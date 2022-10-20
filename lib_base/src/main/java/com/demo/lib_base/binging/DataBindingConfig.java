package com.demo.lib_base.binging;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

/**
 * @author: xuan
 * @Description: com.demo.lib_base.binging
 * @since: 2021/11/11 18:44
 */
public class DataBindingConfig {
    public int layout;
    public Class modelClass;

    public <V extends ViewDataBinding>DataBindingConfig(@NonNull Integer layout, @NonNull Class<V> modelClass) {
        this.layout = layout;
        this.modelClass = modelClass;
    }
    public int getLayout() {
        return this.layout;
    }
}
