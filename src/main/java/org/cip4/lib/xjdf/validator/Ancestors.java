package org.cip4.lib.xjdf.validator;

import jdk.nashorn.internal.ir.annotations.Ignore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Ancestors {

    private List<Object> ancestors;

    public Ancestors() {
        ancestors = Collections.emptyList();
    }

    public Ancestors(final Object... ancestors) {
        this.ancestors = Arrays.asList(ancestors);
    }

    private Ancestors(final List<Object> ancestors) {
        this.ancestors = ancestors;
    }

    public Ancestors push(Object child) {
        List<Object> ancestors = new ArrayList<>(this.ancestors);
        ancestors.add(child);
        return new Ancestors(Collections.unmodifiableList(ancestors));
    }

    @SuppressWarnings("unchecked")
    public <T> T getNearestAncestor(Class<T> type) {
        for (int i = ancestors.size()-1; i>=0; i--) {
            if (type.isInstance(ancestors.get(i))) {
                return (T) ancestors.get(i);
            }
        }
        throw new IllegalArgumentException(String.format(
            "No ancestor of type '%s' found.",
            type.getCanonicalName()
        ));
    }

}
