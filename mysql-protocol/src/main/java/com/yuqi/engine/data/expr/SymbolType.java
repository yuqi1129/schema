package com.yuqi.engine.data.expr;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/8/20 15:40
 **/
public enum SymbolType {

    /**
     * Function
     */
    FUNCTION,

    /**
     * Literal
     */
    LITERAL,

    /**
     * REFERENCE
     *
     * for select directly
     */
    REFERNCE,

    /**
     * INPUT_COLUMN
     * for tablescan
     */
    INPUT_COLUMN;
}
