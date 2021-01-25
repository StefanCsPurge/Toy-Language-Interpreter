package Model.value;

import Model.type.Type;
import Model.type.IntType;


public class IntValue implements Value{

    private final int val;

    public IntValue(int value){ this.val = value; }

    public int getValue() { return val; }

    @Override
    public Type getType() { return new IntType(); }

    @Override
    public String toString() {
        return String.valueOf(val);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof IntValue;
    }
}
