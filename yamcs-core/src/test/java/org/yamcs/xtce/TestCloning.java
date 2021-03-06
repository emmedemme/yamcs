package org.yamcs.xtce;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.Test;
import org.yamcs.xtceproc.XtceDbFactory;

/**
 * Test copy of parameter types
 * @author nm
 *
 */
public class TestCloning {
    @Test
    public void test1() throws IllegalArgumentException, IllegalAccessException {
        XtceDb xtceDb  = XtceDbFactory.createInstanceByConfig("refmdb");
        for(Parameter p: xtceDb.getParameters()) {
            ParameterType t1 = p.getParameterType();
            if(t1==null) continue;
            ParameterType t2 = clone(t1);
            assertNotEquals(t1, t2);
            try {
                verifyEquals(t1, t2);
            } catch (AssertionError e) {
                throw new AssertionError("failed to verify "+t1+": "+e.getMessage());
            }
        }
    }



    ParameterType clone(ParameterType ptype) {
        if(ptype instanceof BinaryParameterType) {
            BinaryParameterType t = (BinaryParameterType) ptype;
            return new BinaryParameterType(t);

        } else if (ptype instanceof BooleanParameterType) {
            BooleanParameterType t = (BooleanParameterType) ptype;
            return new BooleanParameterType(t);

        } else if (ptype instanceof EnumeratedParameterType) {
            EnumeratedParameterType t = (EnumeratedParameterType) ptype;
            return new EnumeratedParameterType(t);

        } else if (ptype instanceof FloatParameterType) {
            FloatParameterType t = (FloatParameterType) ptype;
            return new FloatParameterType(t);

        } else if (ptype instanceof IntegerParameterType) {
            IntegerParameterType t = (IntegerParameterType) ptype;
            return new IntegerParameterType(t);

        } else if (ptype instanceof StringParameterType) {
            StringParameterType t = (StringParameterType) ptype;
            return new StringParameterType(t);
        } else {
            throw new IllegalArgumentException("Cannot clone type "+ptype);
        }
    }


    private void verifyEquals(ParameterType p1, ParameterType p2) throws IllegalArgumentException, IllegalAccessException {
        Class c1 = p1.getClass();
        Class c2 = p2.getClass();
        while(true) {
            assertEquals(c1, c2);
            Field[] fa = c1.getDeclaredFields();
            for(Field f:fa) {
                f.setAccessible(true);
                assertEquals(f.get(p1), f.get(p2));
            }
            c1 = c1.getSuperclass();
            c2 = c2.getSuperclass();
            if(c1==null) {
                break;
            }
        }
    }
}
