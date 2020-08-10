package com.yuqi.storage.block;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/7/20 16:44
 **/
public class StringBlock extends AbstractBlock<String> {

    //由于string 是变长的, 需要独立搞一个适应string 类型的block
    public StringBlock(int size) {
        super(size);
    }

    @Override
    public boolean canAdd() {
        return false;
    }

    @Override
    public void add(String v) {

    }

    @Override
    public String read(int index) {
        return null;
    }
}
