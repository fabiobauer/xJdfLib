package org.cip4.lib.xjdf.util;

import org.cip4.lib.xjdf.schema.SetType;
import org.cip4.lib.xjdf.xml.XJdfConstants;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Wrapper around a list of SetType entries wrapped in JAXBElements.
 * This class provides transparent access to the values of the JAXBElements.
 */
public class SetTypeWrapper implements List<SetType> {

    /**
     * List that is wrapped.
     */
    private List<JAXBElement<? extends SetType>> wrappedList;

    /**
     * Constructor.
     *
     * @param wrappedList List that should be wrapped.
     */
    public SetTypeWrapper(final List<JAXBElement<? extends SetType>> wrappedList) {
        this.wrappedList = wrappedList;
    }

    @Override
    public final int size() {
        return wrappedList.size();
    }

    @Override
    public final boolean isEmpty() {
        return wrappedList.isEmpty();
    }

    @Override
    public final boolean contains(final Object o) {
        throw new RuntimeException("Method not implemented.");
    }

    @NotNull
    @Override
    public final Iterator<SetType> iterator() {
        final Iterator<JAXBElement<? extends SetType>> wrappedIterator = wrappedList.iterator();
        return new Iterator<SetType>() {
            @Override
            public boolean hasNext() {
                return wrappedIterator.hasNext();
            }

            @Override
            public SetType next() {
                return wrappedIterator.next().getValue();
            }

            @Override
            public void remove() {
                wrappedIterator.remove();
            }
        };
    }

    @NotNull
    @Override
    public final Object[] toArray() {
        throw new RuntimeException("Method not implemented.");
    }

    @NotNull
    @Override
    public final <T1> T1[] toArray(@NotNull final T1[] a) {
        throw new RuntimeException("Method not implemented.");
    }

    @Override
    public final boolean add(final SetType t) {
        return wrappedList.add(wrapEntry(t));
    }

    @Override
    public final boolean remove(final Object o) {
        throw new RuntimeException("Method not implemented.");
    }

    @Override
    public final boolean containsAll(@NotNull final Collection<?> c) {
        throw new RuntimeException("Method not implemented.");
    }

    @Override
    public final boolean addAll(@NotNull final Collection<? extends SetType> c) {
        throw new RuntimeException("Method not implemented.");
    }

    @Override
    public final boolean addAll(final int index, @NotNull final Collection<? extends SetType> c) {
        throw new RuntimeException("Method not implemented.");
    }

    @Override
    public final boolean removeAll(@NotNull final Collection<?> c) {
        throw new RuntimeException("Method not implemented.");
    }

    @Override
    public final boolean retainAll(@NotNull final Collection<?> c) {
        throw new RuntimeException("Method not implemented.");
    }

    @Override
    public final void clear() {
        wrappedList.clear();
    }

    @Override
    public final SetType get(final int index) {
        throw new RuntimeException("Method not implemented.");
    }

    @Override
    public final SetType set(final int index, final SetType element) {
        throw new RuntimeException("Method not implemented.");
    }

    @Override
    public final void add(final int index, final SetType element) {
        throw new RuntimeException("Method not implemented.");
    }

    @Override
    public final SetType remove(final int index) {
        throw new RuntimeException("Method not implemented.");
    }

    @Override
    public final int indexOf(final Object o) {
        throw new RuntimeException("Method not implemented.");
    }

    @Override
    public final int lastIndexOf(final Object o) {
        throw new RuntimeException("Method not implemented.");
    }

    @NotNull
    @Override
    public final ListIterator<SetType> listIterator() {
        throw new RuntimeException("Method not implemented.");
    }

    @NotNull
    @Override
    public final ListIterator<SetType> listIterator(final int index) {
        throw new RuntimeException("Method not implemented.");
    }

    @NotNull
    @Override
    public final List<SetType> subList(final int fromIndex, final int toIndex) {
        throw new RuntimeException("Method not implemented.");
    }

    /**
     * Wrap an entry within a corresponding JAXBElement.
     *
     * @param value Entry to wrap.
     *
     * @return JAXBElement with value assigned to the provided entry.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    final JAXBElement<SetType> wrapEntry(final SetType value) {
        String paramName = value.getClass().getSimpleName();
        QName qname = new QName(XJdfConstants.NAMESPACE_JDF20, paramName);
        return new JAXBElement(qname, value.getClass(), null, value);
    }
}
