package backend;

import frontend.Symbol;

public interface IValueSetter {
    public void setValue (Object object) throws Exception;
    public Symbol getSymbol();
}
