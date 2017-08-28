package org.deeplearning4j.nn.conf.constraint;

import lombok.AllArgsConstructor;
import org.deeplearning4j.nn.api.Layer;
import org.deeplearning4j.nn.api.ParamInitializer;
import org.deeplearning4j.nn.api.layers.LayerConstraint;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Broadcast;
import org.nd4j.linalg.indexing.BooleanIndexing;
import org.nd4j.linalg.indexing.conditions.Conditions;

import java.util.Map;


@AllArgsConstructor
public abstract class BaseConstraint implements LayerConstraint {
    public static final double DEFAULT_EPSILON = 1e-6;

    protected boolean applyToWeights;
    protected boolean applyToBiases;
    protected double epsilon = 1e-6;
    protected int[] dimensions;

    protected BaseConstraint(boolean applyToWeights, boolean applyToBiases, int... dimensions){
        this(applyToWeights, applyToBiases, DEFAULT_EPSILON, dimensions);
    }


    @Override
    public void applyConstraint(Layer layer, int iteration, int epoch) {
        Map<String,INDArray> paramTable = layer.paramTable();
        if(paramTable == null || paramTable.isEmpty() ){
            return;
        }

        ParamInitializer i = layer.conf().getLayer().initializer();
        for(Map.Entry<String,INDArray> e : paramTable.entrySet()){
            if(applyToWeights && i.isWeightParam(e.getKey()) || applyToBiases && i.isBiasParam(e.getKey())){
                apply(e.getValue(), i.isBiasParam(e.getKey()));
            }
        }
    }

    public abstract void apply(INDArray param, boolean isBias);
}
