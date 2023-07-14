package com.yuqi.storage.engine;

import org.apache.calcite.schema.Table;

//import javax.annotation.PostConstruct;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 10/8/20 18:54
 **/
public class TableStorgeEngine {
    private RealTimeEngine realTimeEngine;
    private BlockEngine blockEngine;


    private Table table;


//    @PostConstruct
    public void init() {
        //load data to restore blockEngine and realtime Engine
    }
}
