package Model.type;

import Model.value.RefValue;
import Model.value.Value;

public class RefType implements Type{
    Type inner;

    public RefType(Type inner) { this.inner = inner; }

    public Type getInner() {return inner;}

    @Override
    public boolean equals(Object another) {
        if (another instanceof RefType)
            return inner.equals(((RefType) another).getInner());
        return false;
    }

    @Override
    public String toString() { return "Ref(" + inner.toString() + ")"; }

    @Override
    public Value defaultValue() { return new RefValue(0,inner); }
}
