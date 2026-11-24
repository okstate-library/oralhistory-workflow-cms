package com.okstatelibrary.oralhistoryworkflowcms.entity.elasticssearch;

import java.util.ArrayList;

public class Hit {
    public String _index;
    public String _id;
    public double _score;
    public Source _source;
    public Total total;
    public double max_score;
    public ArrayList<Hit> hits;
}
