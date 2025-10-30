package com.shosholanda.fc;

import java.util.Random;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

/**
 * Pruebas unitarias para @CurvaEliptica
 */
public class TestCurvaEliptica {

    private Random r;
    private Punto p;
    private final CurvaEliptica e0 = new CurvaEliptica();
    private final CurvaEliptica e1 = new CurvaEliptica(2, 2, 17);
    private final CurvaEliptica e2 = new CurvaEliptica(2, 9, 37);
    private final CurvaEliptica e3 = new CurvaEliptica(-2, 8, 233);
    private int max;
    private final double epsilon = 0.00001;

    public TestCurvaEliptica(){
	r = new Random();
	p = new Punto();
	max = r.nextInt(Integer.MAX_VALUE);
    }

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    private Punto getPrimerPunto(CurvaEliptica e){
	int p = e.getPrimo();
	int a = e.getA();
	int b = e.getB();
	for (int x = 0; x < p; x++)
	    for (int y = 0; y < p; y++)
		if (y*y%p == (x*x*x + a*x + b)%p)
		    return new Punto(x, y);
	return null;
    }


    @Test
    public void testConstructor() {
	CurvaEliptica e = e0;
	Assert.assertTrue(e.getA() == 1);
	Assert.assertTrue(e.getB() == 1);
	Assert.assertTrue(e.getPrimo() == 3);

	
	try {
	    e = new CurvaEliptica(3, 2, 22);
	    Assert.fail();
	} catch (IllegalArgumentException iae){ }

	max = 1<<9;
	for (int i = -max; i < max; i++)
	    for (int j = -max; j < max; j++)
		if (4*i*i*i + 27*j*j == 0)
		    try {
			e = new CurvaEliptica(i, j, 2);
			Assert.fail();
		    } catch (IllegalArgumentException iae){ }
    }

    @Test
    public void testToString(){
	Assert.assertTrue(e0.toString().equals("y² ≡ x³ + 1x + 1   mod 3"));
	Assert.assertTrue(e1.toString().equals("y² ≡ x³ + 2x + 2   mod 17"));
	Assert.assertTrue(e2.toString().equals("y² ≡ x³ + 2x + 9   mod 37"));
	Assert.assertTrue(e3.toString().equals("y² ≡ x³ + -2x + 8   mod 233"));
    }

    @Test
    public void testPuntos(){
	Assert.assertTrue(e0.puntos().contains(null));
	Assert.assertTrue(e0.puntos().size() == 4);
	Punto[] f = {null, new Punto(0,1), new Punto(0,2), new Punto(1,0)};
	for (Punto p : f)
	    Assert.assertTrue(e0.puntos().contains(p));
	
	Punto[] g = {null, new Punto(0, 6), new Punto(0, 11), new Punto(3, 1), new Punto(3, 16),
	    new Punto(5, 1), new Punto(5, 16), new Punto(6, 3), new Punto(6, 14),
	    new Punto(7, 6), new Punto(7, 11), new Punto(9, 1), new Punto(9, 16),
	    new Punto(10, 6), new Punto(10, 11), new Punto(13, 7), new Punto(13, 10),
	    new Punto(16, 4), new Punto(16, 13)};
	
	Assert.assertTrue(g.length == e1.puntos().size());
	for (Punto p: g)
	    Assert.assertTrue(e1.puntos().contains(p));

	Assert.assertTrue(e3.puntos().contains(new Punto(188, 141)));
	Assert.assertTrue(e3.puntos().contains(new Punto(232, 230)));
	Assert.assertFalse(e3.puntos().contains(new Punto(0, 5)));
	Assert.assertTrue(e3.puntos().size() == 216);
    }

    @Test
    public void testPertenece(){
	Assert.assertTrue(e0.pertenece(null));
	
	Punto[] f = {null, new Punto(0,1), new Punto(0,2), new Punto(1,0)};
	for (Punto p : f)
	    Assert.assertTrue(e0.pertenece(p));

	for (Punto p: e3.puntos())
	    Assert.assertTrue(e3.pertenece(p));

	p = e3.puntos().get(0);
	Punto q = p;
	while (q != null){
	    Assert.assertTrue(e3.pertenece(p));
	    Assert.assertTrue(e3.pertenece(q));
	    q = e3.suma(p, q);
	    Assert.assertTrue(e3.pertenece(q));
	}
    }

    @Test
    public void testSuma(){
	Punto infinito = null;
	Punto q = null;
	p  = q;
	Assert.assertTrue(e0.pertenece(p));
	Assert.assertTrue(e0.pertenece(q));
	Assert.assertTrue(e0.pertenece(e0.suma(p,q)));

	p = new Punto(0, 1);
	Assert.assertTrue(e0.pertenece(p));
	Assert.assertTrue(e0.suma(p, q) == p);
	Assert.assertTrue(e0.suma(q, p) == p);

	q = new Punto(0, 2);
	Assert.assertTrue(e0.pertenece(q));
	Assert.assertTrue(e0.suma(p, q) == infinito);
	Assert.assertTrue(e0.suma(q, p) == infinito);

	p = new Punto(1, 0);
	Assert.assertTrue(e0.pertenece(p));
	Assert.assertTrue(e0.suma(p, p) == infinito);


	Punto[] f = {new Punto(5,1), new Punto(6, 3), new Punto(10, 6), new Punto(3, 1),
	    new Punto(9, 16), new Punto(16, 13), new Punto(0, 6), new Punto(13, 7),
	    new Punto(7, 6), new Punto(7, 11), new Punto(13, 10), new Punto (0, 11),
	    new Punto(16, 4), new Punto(9, 1), new Punto(3, 16), new Punto(10, 11),
	    new Punto(6, 14), new Punto(5, 16), null};

	p = new Punto(5, 1);
	for (int i = 0; i < f.length; i++){
	    q = f[i];
	    Punto r = e1.suma(p, q);
	    if (r != null)
		Assert.assertTrue(e1.suma(p, q).equals(f[(i+1)%f.length]));
	    else
		Assert.assertTrue(e1.suma(p, q) == infinito);
	}
    }

    @Test
    public void testMultiplicacion(){
	Punto infinito = null;
	p = null;

	Assert.assertTrue(e1.pertenece(p));
	Assert.assertTrue(e1.multiplicacion(-1, p) == infinito);
	Assert.assertTrue(e1.multiplicacion(Integer.MAX_VALUE, p) == infinito);

	Punto[] f = {new Punto(5,1), new Punto(6, 3), new Punto(10, 6), new Punto(3, 1),
	    new Punto(9, 16), new Punto(16, 13), new Punto(0, 6), new Punto(13, 7),
	    new Punto(7, 6), new Punto(7, 11), new Punto(13, 10), new Punto (0, 11),
	    new Punto(16, 4), new Punto(9, 1), new Punto(3, 16), new Punto(10, 11),
	    new Punto(6, 14), new Punto(5, 16), null};

	p = f[0];
	Punto q = f[f.length-2];
	Assert.assertTrue(e1.suma(p, q) == infinito);
	Assert.assertTrue(e1.multiplicacion(0, p).equals(p));
	Assert.assertTrue(e1.multiplicacion(1, p).equals(p));
	Assert.assertTrue(e1.multiplicacion(-1, p).equals(q));

	int numPuntos = f.length-2;
	for (int i = 0; i < numPuntos; i++){
	    Punto pos = new Punto(f[i].getX(), f[i].getY());
	    Punto neg = new Punto(f[numPuntos-i].getX(), f[numPuntos-i].getY());
	    Assert.assertTrue(e1.multiplicacion(i+1, p).equals(pos));
	    Assert.assertTrue(e1.multiplicacion(i+1, q).equals(neg));
	    Assert.assertTrue(e1.multiplicacion(-i-1, p).equals(neg));
	    Assert.assertTrue(e1.multiplicacion(-i-1, q).equals(pos));
	}

	for (int i = 0; i < f.length-1; i++)
	    Assert.assertTrue(e1.multiplicacion(i+1, p).equals(f[i]));

	CurvaEliptica[] curvas = {e0, e1, e2, e3};
	
	for (CurvaEliptica e : curvas){
	    List<Punto> puntos = e.puntos();
	    numPuntos = puntos.size();
	    for (Punto p: puntos){
		Assert.assertTrue(e.multiplicacion(numPuntos, p) == infinito);
		if (p == null)
		    q = p;
		else
		    q = new Punto(p.getX(), (p.getY() + e.getPrimo()) % e.getPrimo());
		Assert.assertTrue(e.multiplicacion(-numPuntos, q) == infinito);
	    }
	}
    }

    @Test
    public void testOrden(){
	// Pensé que e3 era una buena curva.
	CurvaEliptica[] curvas = {e1, e2};
	for (CurvaEliptica c : curvas){
	    try {
		c.orden(new Punto(0, 0));
		Assert.fail();
	    } catch (IllegalArgumentException iae){ }
	    List<Punto> puntos = c.puntos();
	    for (Punto p: puntos)
		if (p == null)
		    Assert.assertTrue(c.orden(p) == Integer.MAX_VALUE);
		else
		    Assert.assertTrue(c.orden(p) == puntos.size());
	}
	CurvaEliptica e = new CurvaEliptica(2, 1, 31);
	for (Punto p: e.puntos())
	    Assert.assertTrue(e.orden(p) > 1);
    }

    @Test
    public void testCofactor(){
	// CHECK
	CurvaEliptica[] curvas = {e1, e2};

	for (CurvaEliptica c: curvas){
	    try {
		c.cofactor(new Punto(0, 0));
		Assert.fail();
	    } catch (IllegalArgumentException iae){ }
	    for (Punto p: c.puntos())
		Assert.assertTrue( p == null ?
				   c.cofactor(p) < 0 + epsilon:
				   c.cofactor(p) == 1);
	}

	CurvaEliptica[] basura = {
	    new CurvaEliptica(23, 12, 19), new CurvaEliptica(7, 7, 7),
	    new CurvaEliptica(3, 23, 311), new CurvaEliptica(3, 213, 31)
	};
	
	for (CurvaEliptica e: basura)
	    for (Punto p: e.puntos())
		if (p == null)
		    Assert.assertTrue(e.cofactor(p) < 0 + epsilon);
		else
		    Assert.assertTrue(e.cofactor(p) > 1);
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
