package com.yuqi.sql.rel;

import com.yuqi.engine.operator.Operator;
import org.apache.calcite.rel.RelNode;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 27/7/20 20:54
 **/
public interface SlothRel<R> extends RelNode {

    //add some implement method

    default Operator<R> implement() {
        return null;
    }
}
