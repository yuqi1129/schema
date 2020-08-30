package com.yuqi.engine.data.type;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public final class DataTypes {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataTypes.class);

    public static final ByteType BYTE = ByteType.INSTANCE;
    public static final BooleanType BOOLEAN = BooleanType.INSTANCE;

    public static final StringType STRING = StringType.INSTANCE;

    public static final DoubleType DOUBLE = DoubleType.INSTANCE;
    public static final FloatType FLOAT = FloatType.INSTANCE;

    public static final ShortType SHORT = ShortType.INSTANCE;
    public static final IntegerType INTEGER = IntegerType.INSTANCE;
    public static final LongType LONG = LongType.INSTANCE;
    public static final DateType DATE = DateType.INSTANCE;

    public static final TimestampType TIMESTAMPZ = TimestampType.INSTANCE_WITH_TZ;
    public static final TimestampType TIMESTAMP = TimestampType.INSTANCE_WITHOUT_TZ;

    public static final Set<String> PRIMITIVE_TYPE_NAMES_WITH_SPACES = Sets.newHashSet(
            TIMESTAMPZ.getName(),
            TIMESTAMP.getName(),
            DOUBLE.getName()
    );

    public static final List<DataType> PRIMITIVE_TYPES = Lists.newArrayList(
            BYTE,
            BOOLEAN,
            STRING,
            DOUBLE,
            FLOAT,
            SHORT,
            INTEGER,
            LONG,
            TIMESTAMPZ,
            TIMESTAMP
    );

    public static final Set<DataType> STORAGE_UNSUPPORTED = Sets.newHashSet(
    );

    public static final List<DataType> NUMERIC_PRIMITIVE_TYPES = Lists.newArrayList(
        DOUBLE,
        FLOAT,
        BYTE,
        SHORT,
        INTEGER,
        LONG
    );

    public static final Set<DataType> INTEGER_TYPES = Sets.newHashSet(
            BYTE,
            SHORT,
            INTEGER,
            LONG);

    public static final Set<DataType> DECIMAL_TYPES = Sets.newHashSet(
            FLOAT,
            DOUBLE);

    public static final Set<DataType> DATE_TIME_TYPES = Sets.newHashSet(
            DATE,
            TIMESTAMP
    );

    private static final Set<Integer> NUMBER_CONVERSIONS = Stream.concat(
        Stream.of(BOOLEAN, STRING, TIMESTAMPZ, TIMESTAMP),
        NUMERIC_PRIMITIVE_TYPES.stream()
    ).map(DataType::id).collect(toSet());


    static final Map<Integer, Set<Integer>> ALLOWED_CONVERSIONS = Maps.newHashMap();
    static {
        ALLOWED_CONVERSIONS.put(BYTE.id(), NUMBER_CONVERSIONS);
        ALLOWED_CONVERSIONS.put(SHORT.id(), NUMBER_CONVERSIONS);
        ALLOWED_CONVERSIONS.put(INTEGER.id(), NUMBER_CONVERSIONS);
        ALLOWED_CONVERSIONS.put(LONG.id(), NUMBER_CONVERSIONS);
        ALLOWED_CONVERSIONS.put(FLOAT.id(), NUMBER_CONVERSIONS);
        ALLOWED_CONVERSIONS.put(DOUBLE.id(), NUMBER_CONVERSIONS);
        ALLOWED_CONVERSIONS.put(BOOLEAN.id(), Sets.newHashSet(STRING.id()));
        ALLOWED_CONVERSIONS.put(STRING.id(), NUMBER_CONVERSIONS);
        ALLOWED_CONVERSIONS.put(TIMESTAMP.id(), Sets.newHashSet(DOUBLE.id(), LONG.id(), STRING.id()));
    }

    /**
     * Contains number conversions which are "safe" (= a conversion would not reduce the number of bytes
     * used to store the value)
     */
    private static final Map<Integer, Set<DataType>> SAFE_CONVERSIONS = Maps.newHashMap();

    static {
        SAFE_CONVERSIONS.put(BYTE.id(), Sets.newHashSet(SHORT, INTEGER, LONG, TIMESTAMPZ, TIMESTAMP, FLOAT, DOUBLE));
        SAFE_CONVERSIONS.put(SHORT.id(), Sets.newHashSet(INTEGER, LONG, TIMESTAMPZ, TIMESTAMP, FLOAT, DOUBLE));

        SAFE_CONVERSIONS.put(INTEGER.id(), Sets.newHashSet(LONG, TIMESTAMPZ, TIMESTAMP, FLOAT, DOUBLE));

        SAFE_CONVERSIONS.put(LONG.id(), Sets.newHashSet(TIMESTAMPZ, TIMESTAMP, DOUBLE));
        SAFE_CONVERSIONS.put(FLOAT.id(), Sets.newHashSet(DOUBLE));

    }


    public static final Map<Class<?>, DataType<?>> POJO_TYPE_MAPPING = Maps.newHashMap();

    static {
        POJO_TYPE_MAPPING.put(Double.class, DOUBLE);
        POJO_TYPE_MAPPING.put(Float.class, FLOAT);

        POJO_TYPE_MAPPING.put(Integer.class, INTEGER);
        POJO_TYPE_MAPPING.put(Long.class, LONG);
        POJO_TYPE_MAPPING.put(Short.class, SHORT);
        POJO_TYPE_MAPPING.put(Byte.class, BYTE);

        POJO_TYPE_MAPPING.put(Boolean.class, BOOLEAN);

        POJO_TYPE_MAPPING.put(String.class, STRING);
    }


    public static DataType<?> guessType(Object value) {
        if (value == null) {
            throw new UnsupportedOperationException("value is null...");
        }
        return POJO_TYPE_MAPPING.get(value.getClass());
    }

    @Nullable
    public static DataType getIntegralReturnType(DataType argumentType) {
        switch (argumentType.id()) {
            case ByteType.ID:
            case ShortType.ID:
            case IntegerType.ID:
            case FloatType.ID:
                return DataTypes.INTEGER;

            case DoubleType.ID:
            case LongType.ID:
                return DataTypes.LONG;

            default:
                return null;
        }
    }


    private static boolean safeConversionPossible(DataType type1, DataType type2) {
        final DataType source;
        final DataType target;
        if (type1.precedes(type2)) {
            source = type2;
            target = type1;
        } else {
            source = type1;
            target = type2;
        }

        Set<DataType> conversions = SAFE_CONVERSIONS.get(source.id());
        return conversions != null && conversions.contains(target);
    }


    public static final Map<String, DataType> TYPES_BY_NAME_OR_ALIAS = Maps.newHashMap();

    static {
        TYPES_BY_NAME_OR_ALIAS.put(BYTE.getName(), BYTE);
        TYPES_BY_NAME_OR_ALIAS.put(SHORT.getName(), SHORT);
        TYPES_BY_NAME_OR_ALIAS.put(INTEGER.getName(), INTEGER);
        TYPES_BY_NAME_OR_ALIAS.put(LONG.getName(), LONG);

        TYPES_BY_NAME_OR_ALIAS.put(FLOAT.getName(), FLOAT);
        TYPES_BY_NAME_OR_ALIAS.put(DOUBLE.getName(), DOUBLE);

        TYPES_BY_NAME_OR_ALIAS.put(STRING.getName(), STRING);

        TYPES_BY_NAME_OR_ALIAS.put(BOOLEAN.getName(), BOOLEAN);

        TYPES_BY_NAME_OR_ALIAS.put(TIMESTAMP.getName(), TIMESTAMP);


        TYPES_BY_NAME_OR_ALIAS.put("int2", SHORT);
        TYPES_BY_NAME_OR_ALIAS.put("int", INTEGER);
        TYPES_BY_NAME_OR_ALIAS.put("int4", INTEGER);
        TYPES_BY_NAME_OR_ALIAS.put("int8", LONG);

        TYPES_BY_NAME_OR_ALIAS.put("long", LONG);

        TYPES_BY_NAME_OR_ALIAS.put("float", FLOAT);
        TYPES_BY_NAME_OR_ALIAS.put("double", DOUBLE);


        TYPES_BY_NAME_OR_ALIAS.put("varchar", STRING);
        TYPES_BY_NAME_OR_ALIAS.put("string", STRING);


    }


    public static DataType<?> ofName(String typeName) {
        DataType<?> dataType = ofNameOrNull(typeName);
        if (dataType == null) {
            throw new IllegalArgumentException("Cannot find data type: " + typeName);
        }
        return dataType;
    }

    @Nullable
    public static DataType<?> ofNameOrNull(String typeName) {
        return TYPES_BY_NAME_OR_ALIAS.get(typeName);
    }


    private static final Map<String, DataType> MAPPING_NAMES_TO_TYPES = Maps.newHashMap();

    static {
        MAPPING_NAMES_TO_TYPES.put("byte", BYTE);
        MAPPING_NAMES_TO_TYPES.put("string", STRING);
        MAPPING_NAMES_TO_TYPES.put("integer", INTEGER);
        MAPPING_NAMES_TO_TYPES.put("short", SHORT);
        MAPPING_NAMES_TO_TYPES.put("long", LONG);

        MAPPING_NAMES_TO_TYPES.put("float", FLOAT);
        MAPPING_NAMES_TO_TYPES.put("double", DOUBLE);
        MAPPING_NAMES_TO_TYPES.put("boolean", BOOLEAN);

    }


    private static final Map<Integer, String> TYPE_IDS_TO_MAPPINGS = Maps.newHashMap();

    static {
        TYPE_IDS_TO_MAPPINGS.put(STRING.id(), "text");
        TYPE_IDS_TO_MAPPINGS.put(BYTE.id(), "byte");
        TYPE_IDS_TO_MAPPINGS.put(BOOLEAN.id(), "boolean");
        TYPE_IDS_TO_MAPPINGS.put(DOUBLE.id(), "double");
        TYPE_IDS_TO_MAPPINGS.put(FLOAT.id(), "float");
        TYPE_IDS_TO_MAPPINGS.put(SHORT.id(), "short");
        TYPE_IDS_TO_MAPPINGS.put(LONG.id(), "long");
        TYPE_IDS_TO_MAPPINGS.put(TIMESTAMP.id(), "date");

    }

    @Nullable
    public static String esMappingNameFrom(int typeId) {
        return TYPE_IDS_TO_MAPPINGS.get(typeId);
    }

    @Nullable
    public static DataType ofMappingName(String name) {
        return MAPPING_NAMES_TO_TYPES.get(name);
    }

    public static boolean isPrimitive(DataType type) {
        return PRIMITIVE_TYPES.contains(type);
    }


    public static Streamer[] getStreamers(Collection<? extends DataType> dataTypes) {
        Streamer[] streamer = new Streamer[dataTypes.size()];
        int idx = 0;
        for (DataType dataType : dataTypes) {
            streamer[idx] = dataType.streamer();
            idx++;
        }
        return streamer;
    }

    public static boolean compareTypesById(DataType<?> left, DataType<?> right) {
        return left.id() == right.id();
    }

    public static boolean compareTypesById(List<DataType> left, List<DataType> right) {
        if (left.size() != right.size()) {
            return false;
        }
        assert left instanceof RandomAccess && right instanceof RandomAccess
            : "data type lists should support RandomAccess for fast lookups";
        for (int i = 0; i < left.size(); i++) {
            if (!compareTypesById(left.get(i), right.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static DataType<?> tryFindNotNullType(List<DataType> dataTypes) {
        return dataTypes.stream()
            .findFirst().orElseThrow(() -> new RuntimeException("Unspported exception ..."));
    }

    public static DataType<?> fromId(Integer id) {
        return TYPES_BY_NAME_OR_ALIAS.values().stream()
            .filter(x -> x.id() == id)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Unspported id: " + id));
    }
}
