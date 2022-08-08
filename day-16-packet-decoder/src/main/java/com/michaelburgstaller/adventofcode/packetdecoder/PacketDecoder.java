package com.michaelburgstaller.adventofcode.packetdecoder;

import com.michaelburgstaller.adventofcode.common.Exercise;

import java.util.*;

public class PacketDecoder extends Exercise {

    private static Map<String, String> hexToBinaryMapping = new HashMap<>() {{
        put("0", "0000");
        put("1", "0001");
        put("2", "0010");
        put("3", "0011");
        put("4", "0100");
        put("5", "0101");
        put("6", "0110");
        put("7", "0111");
        put("8", "1000");
        put("9", "1001");
        put("A", "1010");
        put("B", "1011");
        put("C", "1100");
        put("D", "1101");
        put("E", "1110");
        put("F", "1111");
    }};

    private interface Data {
        Integer getLength();
    }

    private static class Literal implements Data {
        public Integer value;
        public Integer length;

        @Override
        public Integer getLength() {
            return length;
        }

        public Literal(Integer value, Integer length) {
            this.value = value;
            this.length = length;
        }

        public static Literal parse(Decoder decoder) {
            if (!decoder.hasBits()) return new Literal(null, 0);

            var continueParsing = true;
            var binary = new StringBuilder();
            var length = 0;
            while (continueParsing) {
                var prefix = Integer.parseInt(decoder.readBits(1), 2);
                continueParsing = (prefix == 1);
                binary.append(decoder.readBits(4));
                length += 5;
            }

            return new Literal(Integer.parseInt(binary.toString(), 2), length);
        }
    }

    private enum LengthType {
        TOTAL_LENGTH,
        NUMBER_OF_SUBPACKETS;

        public static LengthType parse(String rawValue) {
            return switch (Integer.parseInt(rawValue, 2)) {
                case 0 -> TOTAL_LENGTH;
                case 1 -> NUMBER_OF_SUBPACKETS;
                default -> null;
            };
        }
    }

    private static class Operator implements Data {
        public LengthType lengthType;
        public Integer length;
        public List<Packet> subpackets;

        @Override
        public Integer getLength() {
            return length;
        }

        public Operator(LengthType lengthType, Integer length, List<Packet> subpackets) {
            this.lengthType = lengthType;
            this.length = length;
            this.subpackets = subpackets;
        }

        public static Operator parse(Decoder decoder) {
            if (!decoder.hasBits()) return new Operator(null, 0, Collections.emptyList());

            var lengthType = LengthType.parse(decoder.readBits(1));

            Integer length;
            List<Packet> subpackets = new ArrayList<>();
            switch (lengthType) {
                case TOTAL_LENGTH -> {
                    length = Integer.parseInt(decoder.readBits(15), 2);
                    var parsedBits = 0;
                    while (parsedBits < length - 1) {
                        var packet = Packet.parse(decoder);
                        parsedBits += packet.length;
                        subpackets.add(packet);
                    }
                    break;
                }
                case NUMBER_OF_SUBPACKETS -> {
                    length = Integer.parseInt(decoder.readBits(11), 2);
                    for (var i = 0; i < length; i++) {
                        subpackets.add(Packet.parse(decoder));
                    }
                    length = subpackets.stream().map(packet -> packet.length).reduce(0, Integer::sum);
                    break;
                }
                default -> length = 0;
            }

            return new Operator(lengthType, length, subpackets);
        }
    }

    private static class Packet {
        public Integer version;
        public Integer typeId;
        public Integer length;
        public Data data;

        public Packet(Integer version, Integer typeId, Integer length, Data data) {
            this.version = version;
            this.typeId = typeId;
            this.length = length;
            this.data = data;
        }

        public static Packet parse(Decoder decoder) {
            if (!decoder.hasBits()) return new Packet(null, null, 0, null);

            var version = Integer.parseInt(decoder.readBits(3), 2);
            var typeId = Integer.parseInt(decoder.readBits(3), 2);

            Data data;
            if (typeId == 4) {
                data = Literal.parse(decoder);
            } else {
                data = Operator.parse(decoder);
            }

            return new Packet(version, typeId, (3 + 3 + data.getLength()), data);
        }
    }

    private static class Decoder {
        public List<String> bits;
        public Integer index;

        public Decoder(List<String> bits) {
            this.bits = bits;
            this.index = 0;
        }

        public Boolean hasBits() {
            return index < bits.size();
        }

        public String readBits(Integer n) {
            var builder = new StringBuilder();

            for (var i = 0; i < n && index < bits.size(); i++, index++) {
                builder.append(bits.get(index));
            }

            return builder.toString();
        }

        public static Decoder parse(String rawValue) {
            var hexBits = rawValue.toUpperCase().split("");
            var binaryBits = Arrays.stream(hexBits).map(hexToBinaryMapping::get).flatMap(bit -> Arrays.stream(bit.split(""))).toList();
            return new Decoder(binaryBits);
        }

    }

    public static void main(String[] args) {
        var decoders = getLineStream("example.txt").map(Decoder::parse).toList();

        var packets = decoders.stream().map(Packet::parse).toList();

        System.out.println("Finished!");
    }
}
