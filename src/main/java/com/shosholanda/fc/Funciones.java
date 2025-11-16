package com.shosholanda.fc;

import java.util.Random;

/** Funciones auxiliarles para las curvas elípticas 
 */
public class Funciones {

    /**
     * Realiza la exponencia rapida modular
     * @param base el numero a exponenciar
     * @param exp el grado del exponente
     * @param mod el modulo de la expresion
     * @return
     */
    public static int expMod(int base, int exp, int mod){
        // Exponenciacion rapida modular o binaria
        int resultado = 1; // resultado inicial
        int potenciaActual = base % mod; // Base inicial, debemos asegurar de que es modulo m

        while(exp > 0){
            // si el bit menos significativo es 1, multiplicamos
            if((exp & 1) == 1){
                resultado = (resultado * potenciaActual) % mod;
            }
            // elevamos la potencia actual al cuadrado
            potenciaActual = (potenciaActual * potenciaActual) % mod;
            // desplazamos el exponente a la derecha
            exp = exp >> 1;
        }
        return resultado;
    }

    /**
     * Nos dice si el número pasado como argumento es primo
     * independientemente de su signo.
     * @param n el número a verificar si es primo.
     * @return true si el número es primo, false en otro caso.
     */
    public static boolean esPrimo(int n){

        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0) return false;

        Random rand = new Random(System.currentTimeMillis());
        int d, itMod;
        // con cada test disminuye la probabilidad de que un mentiroso nos mienta, 1/2^12 deberia ser suficientemente bueno en este caso
        for(int i = 0; i < 12; i++){
            d = rand.nextInt(n-1);
            d++;
            itMod = expMod(d, n, n);
            // si el modulo no es d aseguramos que no es primo
            if(itMod != d){
                return false;
            }
        }   
        // Solo llegamos a este punto si nos mintieron 12 veces, cada vez con probabilidad 1/2, no muy probable. (0.002%)
        return true;
    }

    /**
     * Nos da el inverso aditivo módulo m.
     * Es decir a + k es congruente con 0 módulo m.
     * @param a el número a sacar el inverso.
     * @param mod el módulo de donde sacar el inverso.
     * @return i tal que a + i sea congruente con 0 módulo m.
     */
    public static int inversoAditivo(int a, int mod){
        // este es bastante facil, realmente solo tenemos que añadir suficiente para regresar a 0, o sea llegar a m
        return mod - a;
    }

    // # Llamar a la función con el 256 cómo segundo parametro 
    // def euclides_extendido(a, b):
    //     r_aux, r = a, b
    //     s_aux, s = 1, 0
    //     t_aux, t = 0, 1
    //     while r != 0:
    //         residuo = r_aux // r 
    //         r_aux, r = r, r_aux - residuo * r
    //         s_aux, s = s, s_aux - residuo * s
    //         t_aux, t = t, t - residuo * t
    //     # regresamos mcd y los coeficientes
    //     return r_aux, s_aux % b, t_aux % b

    /**
     * Nos da el inverso multiplicativo módulo m.
     * Es decir a * k es congruente con 1 módulo m.
     * @param a el número a sacar el inverso.
     * @param mod el módulo de donde sacar el inverso.
     * @return i tal que a * i sea congruente con 1 módulo m.
     */
    public static int inversoMultiplicativo(int a, int mod){
        // reutilizamos euclides extendido
        int rAux = a;
        int r = mod;
        int sAux = 1;
        int s = 0;
        int tAux = 0;
        int t = 1;
        int residuo;
        while(r != 0){
            residuo = rAux / r;
            rAux = r;
            r = rAux - residuo * r;
            sAux = s;
            s = sAux - residuo * s;
            tAux = t;
            t = tAux - residuo * t;
        }
        return modulo(sAux, mod);
    }

    /**
     * Nos da el módulo porque -a%p no funciona como se espera
     */
    public static int modulo(int n, int mod){
        while(n < 0 && n >= mod){
            if(n < 0)
                n += mod;
            else
                n = n % mod;
        }
        return n;
    }
    	
}
