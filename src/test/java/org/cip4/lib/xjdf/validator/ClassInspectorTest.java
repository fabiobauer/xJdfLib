package org.cip4.lib.xjdf.validator;

import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.annotation.XmlIDREF;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ClassInspectorTest {

    private class StubClass {
        private Object boringField;

        @XmlIDREF
        private Object fieldWithReference;

        public Object getBoringField() {
            return boringField;
        }

        public Object getFieldWithReference() {
            return fieldWithReference;
        }

        public void getVoid(){}

        public Object getArgument(Object o) {
            return o;
        }

        protected StubClass getProtected() {
            return this;
        }

        private StubClass getPrivate() {
            return this;
        }

        StubClass getPackagePrivate() {
            return this;
        }
    }

    private class StubChildClass extends StubClass {
    }

    private ClassInspector stubClassInspector;
    private ClassInspector stubChildClassInspector;

    @Before
    public void setUp() throws Exception {
        stubClassInspector = new ClassInspector(StubClass.class);
        stubChildClassInspector = new ClassInspector(StubChildClass.class);
    }

    @Test
    public void detectFieldWithReference() throws Exception {
        Field fieldWithReference = StubClass.class.getDeclaredField("fieldWithReference");
        assertTrue(stubClassInspector.isReference(fieldWithReference));
    }

    @Test
    public void detectFieldWithoutReference() throws Exception {
        Field boringField = StubClass.class.getDeclaredField("boringField");
        assertFalse(stubClassInspector.isReference(boringField));
    }

    @Test
    public void gettersContainsExactlyTheGetters() throws Exception {
        assertEquals(
            Arrays.asList(
                StubClass.class.getDeclaredMethod("getBoringField"),
                StubClass.class.getDeclaredMethod("getFieldWithReference")
            ),
            stubClassInspector.getGetters()
        );
    }

    @Test
    public void gettersContainsGettersOfParentClass() throws Exception {
        assertEquals(
            Arrays.asList(
                StubClass.class.getDeclaredMethod("getBoringField"),
                StubClass.class.getDeclaredMethod("getFieldWithReference")
            ),
            stubChildClassInspector.getGetters()
        );
    }

    @Test
    public void detectGetterForFieldWithReference() throws Exception {
        Method getterWithReference = StubClass.class.getDeclaredMethod("getFieldWithReference");
        assertTrue(stubClassInspector.isReference(getterWithReference));
    }

    @Test
    public void detectGetterForFieldWithoutReference() throws Exception {
        Method getterWithoutReference = StubClass.class.getDeclaredMethod("getBoringField");
        assertFalse(stubClassInspector.isReference(getterWithoutReference));
    }

    @Test
    public void getFieldByGetter() throws Exception {
        Method getter = StubClass.class.getDeclaredMethod("getBoringField");
        assertEquals(
            StubClass.class.getDeclaredField("boringField"),
            stubClassInspector.getFieldByGetter(getter)
        );
    }

}