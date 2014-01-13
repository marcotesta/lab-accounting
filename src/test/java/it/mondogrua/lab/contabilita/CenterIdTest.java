package it.mondogrua.lab.contabilita;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import it.mondogrua.lab.accounting.CenterId;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class CenterIdTest {
    @Test
    public void testSize() throws Exception {

        List<String> listA = new ArrayList<String>();
        listA.add("#");
        listA.add("pomodoro");
        listA.add("book");
        listA.add("english");
        CenterId idA = new CenterId(listA);

        int expected = 4;

        assertEquals(expected, idA.size());
    }
    @Test
    public void testEquals_equal() throws Exception {

        List<String> listA = new ArrayList<String>();
        listA.add("#");
        listA.add("pomodoro");
        listA.add("book");
        listA.add("english");
        CenterId idA = new CenterId(listA);

        List<String> listB = new ArrayList<String>();
        listB.add("#");
        listB.add("pomodoro");
        listB.add("book");
        listB.add("english");
        CenterId idB = new CenterId(listB);

        assertTrue(idA.equals(idB));
    }

    @Test
    public void testEquals_ChunkNotEqual() throws Exception {

        List<String> listA = new ArrayList<String>();
        listA.add("#");
        listA.add("pomodoro");
        listA.add("book");
        listA.add("english");
        CenterId idA = new CenterId(listA);

        List<String> listB = new ArrayList<String>();
        listB.add("#");
        listB.add("pomodoro");
        listB.add("book");
        listB.add("italian");
        CenterId idB = new CenterId(listB);

        assertFalse(idA.equals(idB));
    }

    @Test
    public void testEquals_differentSize() throws Exception {

        List<String> listA = new ArrayList<String>();
        listA.add("#");
        listA.add("pomodoro");
        listA.add("book");
        listA.add("english");
        CenterId idA = new CenterId(listA);

        List<String> listB = new ArrayList<String>();
        listB.add("#");
        listB.add("pomodoro");
        listB.add("book");
        CenterId idB = new CenterId(listB);

        assertFalse(idA.equals(idB));
    }

    @Test
    public void testStartWith_differentSize() throws Exception {

        List<String> listA = new ArrayList<String>();
        listA.add("#");
        listA.add("pomodoro");
        listA.add("book");
        listA.add("english");
        CenterId idA = new CenterId(listA);

        List<String> listB = new ArrayList<String>();
        listB.add("#");
        listB.add("pomodoro");
        listB.add("book");
        CenterId idB = new CenterId(listB);

        assertTrue(idA.startWith(idB));
    }

    @Test
    public void testStartWith_Equal() throws Exception {

        List<String> listA = new ArrayList<String>();
        listA.add("#");
        listA.add("pomodoro");
        listA.add("book");
        listA.add("english");
        CenterId idA = new CenterId(listA);

        List<String> listB = new ArrayList<String>();
        listB.add("#");
        listB.add("pomodoro");
        listB.add("book");
        listA.add("english");
        CenterId idB = new CenterId(listB);

        assertTrue(idA.startWith(idB));
    }

    @Test
    public void testStartWith_DifferentChunk() throws Exception {

        List<String> listA = new ArrayList<String>();
        listA.add("#");
        listA.add("pomodoro");
        listA.add("book");
        listA.add("english");
        CenterId idA = new CenterId(listA);

        List<String> listB = new ArrayList<String>();
        listB.add("#");
        listB.add("pomodoro");
        listB.add("book");
        listA.add("italian");
        CenterId idB = new CenterId(listB);

        assertTrue(idA.startWith(idB));
    }

    @Test
    public void testTrim_last() throws Exception {

        List<String> listA = new ArrayList<String>();
        listA.add("#");
        listA.add("pomodoro");
        listA.add("book");
        listA.add("english");
        CenterId idA = new CenterId(listA);

        List<String> listB = new ArrayList<String>();
        listB.add("#");
        listB.add("pomodoro");
        listB.add("book");
        CenterId expected = new CenterId(listB);

        assertEquals(expected, idA.trim(3));
    }

    @Test
    public void testTrim_more() throws Exception {

        List<String> listA = new ArrayList<String>();
        listA.add("#");
        listA.add("pomodoro");
        listA.add("book");
        listA.add("english");
        CenterId idA = new CenterId(listA);

        List<String> listB = new ArrayList<String>();
        listB.add("#");
        listB.add("pomodoro");
        CenterId expected = new CenterId(listB);

        assertEquals(expected, idA.trim(2));
    }
}
