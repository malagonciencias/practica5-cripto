package com.shosholanda.fc;

import java.util.List;
import java.util.ArrayList;

/**
 * Clase que crea una curva elíptica usando un campo finito módulo
 * mayor a 3. Las curvas elípticas cumplen la ecuación:
 *
 * y² ≡ x³ + ax + b   mod p
 *
 * Donde a, b, p son números enteros y p un primo positivo
 */

public class CurvaEliptica {

    /* Parámetro a de la curva */
    private int a;
    /* Parámetro b de la curva */
    private int b;
    /* El primo */
    private int primo;


    /**
     * Construye la más pequeña de las curvas elípticas válida por
     * la construcción:  4a³ + 27b² != 0
     */
    public CurvaEliptica(){
        this.a= 1;
        this.b = 1;
        this.primo = 3;
    }

    /**
     * Construye una curva elíptica dados sus parámetros.
     * @param a primer coeficiente
     * @param b el segundo coeficiente
     * @param primo el primo al módulo
     * @throws IllegalArgumentException si no es una curva válida.
     */
    public CurvaEliptica(int a, int b, int primo){
        if((4 * a* a * a + 27 * b * b) == 0)
            throw new IllegalArgumentException("Parametros invalidos");
        if(!Funciones.esPrimo(primo))
            throw new IllegalArgumentException("El modulo debe ser primo");

        this.a = a;
        this.b = b;
        this.primo = primo;
    }
    
    /**
     * Regresa el coeficiente A de esta curva elíptica.
     * @return el coeficiente A de esta curva elíptica.
     */
    public int getA(){
        return this.a;
    }
    
    /**
     * Regresa el coeficiente B de esta curva elíptica.
     * @return el coeficiente B de esta curva elíptica.
     */
    public int getB(){
        return this.b;
    }

    /**
     * Regresa el primo relacionado a esta curva elíptica.
     * @return el primo relacionado a esta curva elíptica.
     */
    public int getPrimo(){
        return this.primo;
    }
    
    /**
     * Nos dice si el punto recibido como parámetro es parte de la
     * curva
     * @param p el punto a verificar si es parte de esta curva
     * @return si el punto pertenece o no a la curva.
     */
    public boolean pertenece(Punto p){
        if (p == null)
            return true;
        return pertenece(p.getX(), p.getY());
    }

    /**
     * Nos dice si los valores recibidos como parámetro son parte de
     * la curva
     */
    private boolean pertenece(int x, int y){
        int p = primo;
        int izq = (y * y) % p;
        int der = ( (x * x % p * x % p) + a * x + b ) % p;
        izq = (izq + p) % p;
        der = (der + p) % p;
        return izq == der;
    }


    /**
     * Nos regresa todos los puntos que pertenecen a esta lista.
     * @return todos los puntos (a, b) que cumplen la congruencia
     */
    public List<Punto> puntos(){
        List<Punto> puntos = new ArrayList<>();
        puntos.add(null); // punto al infinito
        int p = this.primo;
        for (int x = 0; x < p; x++)
            for (int y = 0; y < p; y++)
                if (this.pertenece(x, y))
                    puntos.add(new Punto(x, y));
        return puntos;
    }


    /**
     * Nos genera todos los posibles puntos empezando desde el punto
     * P, sumados consigo mismo hasta llegar al punto al infinito.
     * @param P el punto p que queremos empezar a generar los demás puntos.
     * @return una lista de los puntos generados de P.
     * @throws IllegalArgumentException si el punto P no pertenece a la curva.
     */
    public List<Punto> genera(Punto p){
        List<Punto> aaa = new ArrayList<>();
        return aaa;
    }


    /**
     * Nos regresa la suma de cualquiera dos puntos que pertenecen a
     * la curva elíptica.
     * @param p el primero punto
     * @param q el segundo punto
     * @return la suma de p y q
     * @throws IllegalArgumentException si p o q no son parte de la curva.
     */
    public Punto suma(Punto p, Punto q){
        if (p != null && !pertenece(p)) throw new IllegalArgumentException();
        if (q != null && !pertenece(q)) throw new IllegalArgumentException();

        if (p == null) return q;
        if (q == null) return p;

        if (p.equals(inverso(q)))   // p + (-q) = O
            return null;

        int x1 = p.getX(), y1 = p.getY();
        int x2 = q.getX(), y2 = q.getY();

        int pmod = primo;

        int lambda;

        if (x1 == x2 && y1 == y2) {
            int num = (3 * x1 % pmod * x1 % pmod + a) % pmod;
            int den = (2 * y1) % pmod;
            den = Funciones.inversoMultiplicativo(den, pmod);
            lambda = num * den % pmod;
        } else {
            int num = (y2 - y1 + pmod) % pmod;
            int den = (x2 - x1 + pmod) % pmod;
            den = Funciones.inversoMultiplicativo(den, pmod);
            lambda = num * den % pmod;
        }

        int x3 = (lambda * lambda % pmod - x1 - x2) % pmod;
        if (x3 < 0) x3 += pmod;
        int y3 = (lambda * (x1 - x3 + pmod) % pmod - y1) % pmod;
        if (y3 < 0) y3 += pmod;

        return new Punto(x3, y3);
    }


    /**
     * Suma k veces el punto p consigo mismo. En otras palabras 
     * calcula k * P.
     * @param k el número de veces a multiplicar consigo mismo.
     * @param p el punto a multiplicar k veces consigo mismo.
     * @return el resultado de k*P (suma multiple).
     */
    public Punto multiplicacion(int k, Punto p){
        if (!pertenece(p))
            throw new IllegalArgumentException();

        if (k == 0) return p;
        if (p == null) return null;

        if (k < 0) {
            int y = (p.getY() == 0 ? 0 : primo - p.getY());
            Punto neg = new Punto(p.getX(), y);
            return multiplicacion(-k, neg);
        }

        Punto r = null;
        Punto add = new Punto(p.getX(), p.getY());

        for (int i = 0; i < k; i++)
            r = suma(r, add);

        return r;
    }



    /**
     * Calcula el orden del punto p en esta curva.  El orden de un
     * punto P se calcula como cuántas veces se debe sumar P consigo
     * mismo hasta llegar al punto al infnito. Si P es null, regresa
     * infinito.
     * @param p el punto a sumar hasta el infinito.
     * @return el número de iteraciones hechas.
     * @throws IllegalArgumentException si el punto p no pertenece a la cuva.
     */
    public int orden(Punto p){
        if (!pertenece(p))
            throw new IllegalArgumentException();

        if (p == null)
            return Integer.MAX_VALUE;

        Punto t = p;
        int count = 1;

        while (t != null) {
            t = suma(t, p);
            count++;
        }
        return count;
    }
 
    /**
     * Define el cofactor del punto P sobre esta curva.  El cofactor
     * se calcula como el total de puntos entre el orden de este
     * punto. el Orden no puede ser 0.
     * @param p el punto de donde sacar el cofactor.
     * @return el cofactor de este punto P en la curva.
     */
    public double cofactor(Punto p){
        if (!pertenece(p))
            throw new IllegalArgumentException();

        if (p == null)
            return -1;

        int n = puntos().size();
        int o = orden(p);

        return (double)n / o;
    }
    

    /**
     * Regresa una representación en cadena de la curva elíptica.
     * @return una representación en cadena de la curva elíptica.
     */
    @Override
    public String toString(){
        String repr = "y² ≡ x³ + " + this.a + "x + " + this.b + "   mod " + this.primo;
        return repr;
    }

    /**
     * Nos dice si el objeto recibido es igual a este objeto.
     * @param o el objeto contra quien comparar.
     * @return true si son iguales, false en otro caso.
     */
    @Override
    public boolean equals(Object o){
        //return true;
        if (this == o) return true;
        if (o == null || !(o instanceof CurvaEliptica)) return false;
        
        CurvaEliptica oCast = (CurvaEliptica) o;
        
        return this.a == oCast.a &&
               this.b == oCast.b &&
               this.primo == oCast.primo;
    }


    /**
     * Mëtodo privado que nos regresa el punto inverso de p.
     * tal que p + p' = punto al infinito.
     * @param p el punto de quien sacar el punto inverso.
     * @return el punto inverso de p.
     */
    private Punto inverso(Punto p){
        if (p == null)
            return null; // punto al infinito

        int x = p.getX();
        int y = p.getY();

        int yinverso = (primo - y) % primo;

        return new Punto(x, yinverso);
    }
	
}
    
