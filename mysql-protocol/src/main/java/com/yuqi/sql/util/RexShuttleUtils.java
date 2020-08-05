package com.yuqi.sql.util;

import com.yuqi.engine.data.expr.Symbol;
import com.yuqi.sql.rex.RexToSymbolShuttle;
import org.apache.calcite.rex.RexNode;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/8/20 19:08
 **/
public class RexShuttleUtils {
    public static final RexToSymbolShuttle INSTANCE = new RexToSymbolShuttle();

    public static List<Symbol> rexToSymbox(List<RexNode> rexNodes) {
        return rexNodes.stream()
                .map(rexNode -> rexNode.accept(INSTANCE))
                .collect(Collectors.toList());
    }
}
