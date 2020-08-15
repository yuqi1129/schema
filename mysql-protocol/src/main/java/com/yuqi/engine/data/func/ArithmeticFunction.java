package com.yuqi.engine.data.func;

import com.yuqi.engine.data.type.DataType;
import com.yuqi.engine.data.type.DataTypes;
import com.yuqi.engine.data.value.Value;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 6/8/20 10:09
 **/
public abstract class ArithmeticFunction extends Scalar {

    public ArithmeticFunction() {
        super(2);
    }

    public static final ArithmeticFunction PLUS = new ArithmeticFunction() {
        @Override
        public Value evaluate(List<Value> args, DataType returnType) {
            boolean hasDecimal = args.stream().anyMatch(v -> DataTypes.DECIMAL_TYPES.contains(v.getType()));

            if (hasDecimal) {
                double r = Functions.plus(args.get(0).doubleValue(), args.get(1).doubleValue());
                return new Value(r, returnType);
            }

            long r =  Functions.plus(args.get(0).longValue(), args.get(1).longValue());
            return new Value(r, returnType);
        }
    };

    public static final ArithmeticFunction MINUS = new ArithmeticFunction() {
        @Override
        public Value evaluate(List<Value> args, DataType returnType) {
            boolean hasDecimal = args.stream().anyMatch(v -> DataTypes.DECIMAL_TYPES.contains(v.getType()));

            if (hasDecimal) {
                double r = Functions.minus(args.get(0).doubleValue(), args.get(1).doubleValue());
                return new Value(r, returnType);
            }

            long r =  Functions.minus(args.get(0).longValue(), args.get(1).longValue());
            return new Value(r, returnType);
        }
    };

    public static final ArithmeticFunction MULTIPLY = new ArithmeticFunction() {
        @Override
        public Value evaluate(List<Value> args, DataType returnType) {
            boolean hasDecimal = args.stream().anyMatch(v -> DataTypes.DECIMAL_TYPES.contains(v.getType()));

            if (hasDecimal) {
                double r = Functions.multiply(args.get(0).doubleValue(), args.get(1).doubleValue());
                return new Value(r, returnType);
            }

            long r =  Functions.multiply(args.get(0).longValue(), args.get(1).longValue());
            return new Value(r, returnType);
        }
    };

    public static final ArithmeticFunction DIVIDE = new ArithmeticFunction() {
        @Override
        public Value evaluate(List<Value> args, DataType returnType) {
            boolean hasDecimal = args.stream().anyMatch(v -> DataTypes.DECIMAL_TYPES.contains(v.getType()));

            if (hasDecimal) {
                double r = Functions.divide(args.get(0).doubleValue(), args.get(1).doubleValue());
                return new Value(r, returnType);
            }

            long r =  Functions.divide(args.get(0).longValue(), args.get(1).longValue());
            return new Value(r, returnType);
        }
    };
}
