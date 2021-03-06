package com.cinchapi.runway;

import org.junit.Assert;
import org.junit.Test;

import com.cinchapi.concourse.test.ClientServerTest;
import com.cinchapi.runway.Record;
import com.cinchapi.runway.Required;
import com.cinchapi.runway.Unique;

public class RecordTest extends ClientServerTest {
    
    private Runway runway;

    @Override
    protected String getServerVersion() {
        return "latest";
    }

    @Override
    public void beforeEachTest() {
        runway = Runway.connect("localhost", server.getClientPort(), "admin", "admin");
    }
    
    @Test
    public void testCannotAddDuplicateValuesForUniqueVariable(){
        Mock person = new Mock();
        person.name = "Jeff Nelson";
        Assert.assertTrue(runway.save(person));
        
        Mock person2 = new Mock();
        person2.name = "Jeff Nelson";
        Assert.assertFalse(runway.save(person2));
        
        person2.name = "Jeffery Nelson";
        Assert.assertTrue(runway.save(person2));      
    }
    
    @Test
    public void testCannotSaveNullValueForRequiredVariable(){
        Mock person = new Mock();
        person.age = 23;
        Assert.assertFalse(runway.save(person));
    }
    
    @Test
    public void testNoPartialSaveWhenRequiredVariableIsNull(){
        Mock person = new Mock();
        person.age = 23;
        runway.save(person);
        Assert.assertTrue(client.describe(person.id()).isEmpty());
    }
    
    @Test
    public void testBooleanIsNotStoredAsBase64(){
        Mock person = new Mock();
        person.name = "John Doe";
        person.age = 100;
        runway.save(person);
        person = runway.load(Mock.class, person.id());
        Assert.assertTrue(person.alive);
    }
    
    @Test
    public void testSetDynamicAttribute(){
        Mock person = new Mock();
        person.set("0_2_0", "foo");
        System.out.println(person);
    }

    class Mock extends Record {

        @Unique
        @Required
        public String name;
        
        public Integer age;
        
        public boolean alive = true;

    }

}
