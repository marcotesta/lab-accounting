package it.mondogrua.lab.contabilita;

import static org.junit.Assert.*;
import it.mondogrua.lab.accounting.CenterId;
import it.mondogrua.lab.accounting.Factory;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class FactoryTest {


    @Test
    public void testCreateCenterId() throws Exception {

        Factory factory = new Factory();
        String centerIdString = "#pomodoro:book:english";
        CenterId centerId = factory.createCenterId(centerIdString );

        List<String> list = new ArrayList<String>();
        list.add("#");
        list.add("pomodoro");
        list.add("book");
        list.add("english");
        CenterId expected = new CenterId(list);

        assertEquals(expected, centerId);
    }

}
