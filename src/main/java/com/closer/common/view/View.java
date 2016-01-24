package com.closer.common.view;

/**
 * 定义的公共View供@JsonView使用
 * Created by closer on 2016/1/24.
 */
public class View {

    public interface Detail {
    }

    public interface List {
    }

    public interface Eager {
    }

    public interface EagerDetail extends Eager {
    }

    public interface EagerList extends Eager {
    }
}
