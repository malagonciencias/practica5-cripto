package com.shosholanda.fc;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

/**
 * Pruebas unitarias para @Punto
 */
public class TestPunto {

    private Random r;
    private Punto p;
    private int max;

    public TestPunto(){
	r = new Random();
	p = new Punto();
	max = r.nextInt(Integer.MAX_VALUE);
    }

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);


    @Test
    public void testConstructor() {
	Assert.assertTrue(p.getX() == 0);
	Assert.assertTrue(p.getY() == 0);
	
	int x = r.nextInt(Integer.MAX_VALUE);
	int y = r.nextInt(Integer.MAX_VALUE);
	Punto o = new Punto(x, y);
	Assert.assertTrue(o.getX() == x);
	Assert.assertTrue(o.getY() == y);
    }

    @Test
    public void testToString(){
	Assert.assertTrue(p.toString().equals("(0, 0)"));
	int x = r.nextInt(max);
	int y = r.nextInt(max);

	p = new Punto(x, y);
	String s = p.toString();
	Assert.assertTrue(s.charAt(0) == '(');
	Assert.assertTrue(s.charAt(s.length()-1) == ')');
	s = s.substring(1, s.length()-1);
	String[] nums = s.split(" ");
	Assert.assertTrue(nums.length == 2);
	String xs = nums[0];
	String ys = nums[1];
	Assert.assertTrue(xs.charAt(xs.length()-1) == ',');
	xs = xs.substring(0, xs.length()-1);
	try {
	    int a = Integer.parseInt(xs);
	    int b = Integer.parseInt(ys);
	    Assert.assertTrue(a == x);
	    Assert.assertTrue(b == y);
	} catch (NumberFormatException nfe){
	    Assert.fail();
	}
    }


    @Test
    public void testEquals(){
	Punto q = null;
	Assert.assertTrue(!p.equals(q));
	q = new Punto();
	Assert.assertFalse(p == q);
	Assert.assertTrue(p.equals(q));
	Assert.assertFalse(p.equals('a'));
	p.set(1, -1);
	Assert.assertFalse(p.equals(q));
	q.set(1, -1);
	Assert.assertTrue(p.equals(q));
    }

    @Test
    public void testSet(){
	Assert.assertTrue(p.getX() == 0);
	Assert.assertTrue(p.getY() == 0);
	int x = r.nextInt(max);
	int y = -r.nextInt(max);
	p.set(x, y);
	Assert.assertTrue(p.getX() == x);
	Assert.assertTrue(p.getY() == y);
    }

    @Test
    public void testPuntoInfinito(){
	Assert.assertFalse(p == null);
	p = null;
	Assert.assertTrue(p == null);
    }
}
