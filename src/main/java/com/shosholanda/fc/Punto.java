package com.shosholanda.fc;


/**
 * Clase que representa un punto en un plano 2D. (x, y)
 * Los puntos son discretos, y el valor máximo está acotado por int
 * Un punto al infinto está representado por NULL
 */
public class Punto {

    /* Coordenada en X del punto */
    private int x;
    /* Coordenada en Y del punto */
    private int y;


    /**
     * Construye un punto con valores iniciales (0, 0)
     */
    public Punto(){

    }

    /**
     * Construye un punto respecto a sus parámetros.
     * @param x la coordenada en x del punto.
     * @param y la coordenada en y del punto.
     */
    public Punto(int x, int y){

    }

    /**
     * Regresa la coordenada en X de este punto.
     * @return la coordenada en X de este punto.
     */
    public int getX(){

    }

    /**
     * Regresa la coordenada en Y de este punto.
     * @return la coordenada en Y de este punto.
     */
    public int getY(){

    }

    /**
     * Asigna las nuevas coordenadas X y Y de este punto.
     * @param x la nueva coordenada en X de este punto.
     * @param y la nueva coordenada en Y de este punto.
     */
    public void set(int x, int y){

    }

    /**
     * Regresa una copia de este punto.
     * @return una copia de este punto.
     */
    public Punto copia(){

    }
	

    /**
     * Regresa una representación en cadena de este punto.
     * @return una representación en cadena de este punto.
     */
    @Override
    public String toString(){

    }


    /**
     * Nos dice si este punto es igual al objeto recibido como parámetro.
     * @param o el objeto con quien comparar este objeto.
     * @return true si ambos puntos son iguales, false en otro caso.
     */
    @Override
    public boolean equals(Object o){

    }

}
