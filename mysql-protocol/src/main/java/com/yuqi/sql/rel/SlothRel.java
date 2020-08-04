package com.yuqi.sql.rel;

import com.yuqi.engine.operator.Operator;
import org.apache.calcite.rel.RelNode;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 27/7/20 20:54
 **/
public interface SlothRel extends RelNode {

    //add some implement method

    default Operator implement() {
        return null;
    }
}
