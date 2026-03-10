import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;
/**
 * La clase Torre cuenta con limites de altura y ancho, contiene a las tazas y tapas apilandolas y puede realizar cambios sobre las mismas.
 * 
 * @author Laura Juliana Parra Velandia y Daniel Santiago Morales Perdomo
 * @version 1.1
 */
public class Tower
{
    private final String dimensionSuperaAltura = "Al insertar el nuevo elemento, se supera la altura maxima de la torre.";
    private final String dimensionSuperaAncho = "Al insertar el nuevo elemento, se supera el ancho maximo de la torre.";
    private final String yaExiste = "Ya existe este numero de elemento en la torre.";
    private int maxWidth;
    private int maxHeight;
    private int currentHeight;
    private int currentWidth;
    private ArrayList<Elemento> stack;
    private HashMap<Elemento, Rectangle> visualElements;
    private boolean isVisible;
    private Canvas canvas;
    
    /**
     * Crea una torre con límites máximos de ancho y alto
     * 
     * @param maxWidth ancho maximo de la nueva torre
     * @param maxHeight altura maxima de la nueva torre
     * 
     */
    public Tower(int maxWidth, int maxHeight)
    {
        if (maxWidth <= 0 || maxHeight <= 0) {
            ok('N');
            throw new IllegalArgumentException("Las dimensiones deben ser numeros enteros positivos.");
        }
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        currentHeight = 0;
        currentWidth = 0;
        stack = new ArrayList<Elemento>();
        visualElements = new HashMap<Elemento, Rectangle>();
        isVisible = false;
        ok(' ');
    }
    
    /**
     * Crea una torre apilando tazas desde la mas grande (cups), hasta la mas pequeña
     * 
     * @param cups cantidad de tazas a crear (deben ser positivas)
     */
    public Tower(int cups)
    {
        if (cups <= 0) {
            ok('N');
            throw new IllegalArgumentException("El número de tazas debe ser un numero entero positivo.");
        }
        //Inicializamos valores teniendo en cuenta la altura de las tazas apiladas desde la pequeña a la mas grande (Altura maxima posible)
        maxHeight = cups * cups;
        maxWidth = (2 * cups) - 1;
        currentHeight = (2 * cups) - 1;
        currentWidth = (2 * cups) - 1;
        stack = new ArrayList<Elemento>();
        visualElements = new HashMap<Elemento, Rectangle>();
        isVisible = false;
        for (int i = cups; i >= 1; i--) {
            stack.add(new Cup(i));
        }
        ok(' ');
    }
    
    /**
     * Indica si la última operación fue exitosa
     */
    public static boolean ok(char operacion) {
        if (operacion == 'N') {//Si 'N'o se completo la operacion retornar false, caso contrario se completo retornar true
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * Genera una tupla que define si un elemento es una taza o tapa, y su ancho.
     * 
     */
    private String[] Tupla(Elemento e) {
        String[] tupla =new String[2];
        if (e instanceof Cup) {
                tupla[0] = "C";
            } else {
                tupla[0] = "L";
            }
            tupla[1] = String.valueOf(e.getWidth());
        return tupla;
    }
    
    /**
     * Calcula la altura actual de la torreta
     * 
     */
    private int sumSimulation(ArrayList<String[]> sim) {
        int newCurrentHeight = 0;
        for (int k = 0; k < sim.size(); k++) {
            if (sim.get(k)[0] == "L") {
                newCurrentHeight = newCurrentHeight + 1;
            } else {
                newCurrentHeight = newCurrentHeight + Integer.valueOf(sim.get(k)[1]);
            }
        }
        return newCurrentHeight;
    }
    
    /**
     * Calcula la altura actual de la torreta
     * 
     */
    private int calculateCurrentHeight() {
        if (stack.isEmpty()) {
            return 0;
        }
        ArrayList<String[]> simulacion = new ArrayList<String[]>();
        for (int i = stack.size() - 1;i >= 0; i--) {
            Elemento elemento = stack.get(i);
            String[] agregar = Tupla(elemento);
            int dimension = elemento.getWidth();
            int espacio = elemento.getHeight() - 1;
            if (simulacion.isEmpty()) {
                simulacion.add(agregar);
            } else {
                int inicioEliminar = 0;
                int anchoUltimoElemento = Integer.valueOf(simulacion.get(simulacion.size()-1)[1]);
                if (dimension <= anchoUltimoElemento) {
                    simulacion.add(agregar);
                } else {
                    String[] extra = {null,null};
                    for (int j = simulacion.size() - 1; j >= 0; j--) {
                        if (dimension > Integer.valueOf(simulacion.get(j)[1])) {
                            espacio = espacio - Integer.valueOf(simulacion.get(j)[1]);
                            inicioEliminar = j;
                            if (espacio == 0) {
                                break;
                            }else if (espacio < 0) {
                                extra[0] = "E";
                                extra[1] = String.valueOf(Math.abs(espacio));
                            }
                        } else {
                            break;
                        }
                    }
                    simulacion.subList(inicioEliminar,simulacion.size()).clear();
                    if (extra[0] != null) {
                        simulacion.add(extra);
                    }
                    simulacion.add(agregar);
                }
            }
        }
        return sumSimulation(simulacion);
    }
    
    /**
     * Calcula la altura de la torre si se agregara un nuevo elemento
     * 
     */
    private int calculateHeightWithNewElement(String[] nuevo) {
        if (stack.isEmpty()) {
            return Integer.valueOf(nuevo[1]);
        }
        ArrayList<String[]> simulacion = new ArrayList<String[]>();
        simulacion.add(nuevo);
        for (int i = stack.size() - 1;i >= 0; i--) {
            Elemento elemento = stack.get(i);
            String[] agregar = Tupla(elemento);
            int dimension = elemento.getWidth();
            int espacio = elemento.getHeight() - 1;
            //Inicio de comprobar la altura
            int inicioEliminar = 0;
            int anchoUltimoElemento = Integer.valueOf(simulacion.get(simulacion.size()-1)[1]);
            if (dimension <= anchoUltimoElemento) {
                simulacion.add(agregar);
            } else {
                String[] extra = {null,null};
                for (int j = simulacion.size() - 1; j >= 0; j--) {
                    if (dimension > Integer.valueOf(simulacion.get(j)[1])) {
                        espacio = espacio - Integer.valueOf(simulacion.get(j)[1]);
                        inicioEliminar = j;
                        if (espacio == 0) {
                            break;
                        }else if (espacio < 0) {
                            extra[0] = "E";
                            extra[1] = String.valueOf(Math.abs(espacio));
                        }
                    } else {
                        break;
                    }
                }
                simulacion.subList(inicioEliminar,simulacion.size()).clear();
                if (extra[0] != null) {
                    simulacion.add(extra);
                }
                simulacion.add(agregar);
            }
        }
        return sumSimulation(simulacion);
    }
    
    /**
     * Calcula el ancho actual de la torre
     */
    private int calculateCurrentWidth() {
        if (stack.isEmpty()) {
            return 0;
        }
        int newCurrentWidth = 0;
        for (int i = 0;i < stack.size();i++) {
            int elementWidth = stack.get(i).getWidth();
            if (elementWidth > newCurrentWidth) {
                newCurrentWidth = elementWidth;
            }
        }
        return newCurrentWidth;
    }
    
    /**
     * Agrega una taza con número i a la cima de la torre.
     * Falla si i es inválido, ya existe la taza o no cabe.
     * Actualiza ok según el resultado.
     */
    public void pushCup(int i) 
    {
        int dimension = (2 * (validateCup(i)) ) - 1;
        if (dimension > maxHeight) {
            ok('N');
            throw new IllegalArgumentException(dimensionSuperaAltura);
        }
        if (dimension > maxWidth) {
            ok('N');
            throw new IllegalArgumentException(dimensionSuperaAncho);
        }
        for (Elemento e : stack) {
            if (e instanceof Cup && ((Cup) e).getNumber() == i) {
                ok('N');
                throw new IllegalArgumentException(yaExiste);
            }
        }
        String[] nuevo = {"C",String.valueOf(dimension)};
        int alturaPosible = calculateHeightWithNewElement(nuevo);
        if (alturaPosible > maxHeight) {
            ok('N');
            throw new IllegalArgumentException(dimensionSuperaAltura);
        } else {
            stack.add(new Cup(i));
            currentHeight = alturaPosible;
            currentWidth = calculateCurrentWidth();
            ok(' ');
        }
        if (isVisible) {
            redraw();
        }
    }
    
    /**
     * Elimina la taza superior si existe
     */
    public void popCup() {
        if (stack.isEmpty()) {
            ok('N');
            throw new IllegalArgumentException("La torre esta vacia, no tiene tazas que eliminar.");
        }
        Elemento top = stack.get(stack.size() - 1);
        if (!(top instanceof Cup)) {
            ok('N');
            throw new IllegalArgumentException("El ultimo elemento de la torre es una tapa.");
        }
        stack.remove(stack.size() - 1);
        currentHeight = calculateCurrentHeight();
        currentWidth = calculateCurrentWidth();
        ok(' ');
        if (isVisible) {
            redraw();
        }
    }
    
    /**
     * Agrega una tapa con número i a la cima de la torre.
     * Falla si i es inválido, ya existe la tapa o no cabe.
     * Actualiza ok según el resultado.
     */
    public void pushLid(int i) {
        int dimension = (2 * (validateCup(i)) ) - 1;
        if (dimension > maxHeight) {
            ok('N');
            throw new IllegalArgumentException(dimensionSuperaAltura);
        }
        if (dimension > maxWidth) {
            ok('N');
            throw new IllegalArgumentException(dimensionSuperaAncho);
        }
        for (Elemento e : stack) {
            if (e instanceof Lid && ((Lid) e).getNumber() == i) {
                ok('N');
                throw new IllegalArgumentException(yaExiste);
            }
        }
        String[] nuevo = {"L",String.valueOf(dimension)};
        int alturaPosible = calculateHeightWithNewElement(nuevo);
        if (alturaPosible > maxHeight) {
            ok('N');
            throw new IllegalArgumentException(dimensionSuperaAltura);
        } else {
            stack.add(new Lid(i));
            currentHeight = alturaPosible;
            currentWidth = calculateCurrentWidth();
            ok(' ');
        }
        if (isVisible) {
            redraw();
        }
    }
    
    /**
     * Elimina la tapa superior si existe
     */
    public void popLid() {
        if (stack.isEmpty()) {
            ok('N');
            return;
        }
        Elemento top = stack.get(stack.size() - 1);
        // Solo se elimina si el elemento superior es una tapa
        if (!(top instanceof Lid)) {
            ok('N');
            return;
        }
        stack.remove(stack.size() - 1);
        currentHeight -= top.getHeight();
        ok(' ');
        if (isVisible) redraw();
    }
    
    /**
     * Elimina una taza específica según su número, si la taza tiene su tapa encima, también se elimina
     * Actualiza la altura de la torre y el estado de la operación.
     *
     * @param i número de la taza a eliminar 
     */
    public void removeCup(int i) {
        // Recorrer la pila buscando la taza con el número indicado
        for (int j = 0; j < stack.size(); j++) {
            Elemento actual = stack.get(j);
            // Verificar que sea una taza y que coincida el número
            if (actual instanceof Cup && actual.getNumber() == i) {
                // Si tiene tapa encima
                if (j + 1 < stack.size()) {
                    Elemento arriba = stack.get(j + 1);
                    if (arriba instanceof Lid && arriba.getNumber() == i) {
                        currentHeight -= arriba.getHeight();
                        stack.remove(j + 1);
                        ok(' ');
                    }
                }
                // Actualizar la altura eliminando la taza
                currentHeight -= actual.getHeight();
                stack.remove(j);
                if (isVisible) {
                    redraw();
                }
                ok('N');
                return;
            }
        }
    }
    
    /**
     * Elimina una tapa específica según su número, si la tapa existe en la torre, se elimina y se actualiza la altura
     * Si no existe, la operación falla.
     *
     * @param i número de la tapa a eliminar
     */
    public void removeLid(int i) {
        // Recorrer la pila buscando la tapa con el número indicado
        for (int j = 0; j < stack.size(); j++) {
            Elemento actual = stack.get(j);
            // Verificar que sea una tapa y que coincida el número
            if (actual instanceof Lid && actual.getNumber() == i) {
                // Actualizar la altura total de la torre
                currentHeight -= actual.getHeight();
                // Eliminar la tapa de la pila
                stack.remove(j);
                ok(' ');
                if (isVisible) {
                    redraw();
                }
                ok('N');
                return;
            }
        }
    }
    
    public void swap(String[] o1, String[] o2) 
    {
        if (o1 == null || o2 == null || o1.length < 2 || o2.length < 2) {
            ok('N');
            return;
        }

        int idx1 = findIndex(o1[0], o1[1]);
        int idx2 = findIndex(o2[0], o2[1]);

        if (idx1 == -1 || idx2 == -1 || idx1 == idx2) {
            ok('N');
            return;
        }

        // Intercambiar en la pila
        Elemento temp = stack.get(idx1);
        stack.set(idx1, stack.get(idx2));
        stack.set(idx2, temp);
        ok(' ');
        if (isVisible) redraw();
    }
    
    /**
     *  Ordena la torre agrupando taza+tapa por número y organizándolas de mayor a menor. 
     */
    public void orderTower() {
        // Separa tazas y tapas
        ArrayList<Cup> cups = new ArrayList<Cup>();
        ArrayList<Lid> lids = new ArrayList<Lid>();
        for (int i = 0; i < stack.size(); i++) {
            Elemento e = stack.get(i);
            if (e instanceof Cup) {
                cups.add((Cup) e);
            } else if (e instanceof Lid) {
                lids.add((Lid) e);
            }
        }
        // Crear bloques (taza + posible tapa)
        ArrayList<ArrayList<Elemento>> bloques = new ArrayList<ArrayList<Elemento>>();
        for (int i = 0; i < cups.size(); i++) {
            Cup cup = cups.get(i);
            int numero = cup.getNumber();
            ArrayList<Elemento> bloque = new ArrayList<Elemento>();
            bloque.add(cup);
            for (int j = 0; j < lids.size(); j++) {
                if (lids.get(j).getNumber() == numero) {
                    bloque.add(lids.get(j));
                    lids.remove(j);
                    break;
                }
            }
            bloques.add(bloque);
        }
        // Tapas que quedaron solas
        for (int i = 0; i < lids.size(); i++) {
            ArrayList<Elemento> bloque = new ArrayList<Elemento>();
            bloque.add(lids.get(i));
            bloques.add(bloque);
        }
        // Ordenar bloques de mayor a menor
        for (int a = 0; a < bloques.size(); a++) {
            for (int b = a + 1; b < bloques.size(); b++) {
                int numA = bloques.get(a).get(0).getNumber();
                int numB = bloques.get(b).get(0).getNumber();
                if (numB > numA) {
                    ArrayList<Elemento> temp = bloques.get(a);
                    bloques.set(a, bloques.get(b));
                    bloques.set(b, temp);
                }
            }
        }
        //Reconstruir torre respetando límites
        ArrayList<Elemento> nueva = new ArrayList<Elemento>();
        int nuevaAltura = 0;

        for (int i = 0; i < bloques.size(); i++) {
            ArrayList<Elemento> bloque = bloques.get(i);
            int alturaBloque = 0;
            boolean cabe = true;
            for (int j = 0; j < bloque.size(); j++) {
                Elemento e = bloque.get(j);
                if (e.getWidth() > maxWidth) {
                    cabe = false;
                    break;
                }
                alturaBloque += e.getHeight();
            }
            if (cabe && nuevaAltura + alturaBloque <= maxHeight) {
                for (int j = 0; j < bloque.size(); j++) {
                    nueva.add(bloque.get(j));
                }
                nuevaAltura += alturaBloque;
            }
        }
        stack = nueva;
        currentHeight = nuevaAltura;
        ok(' ');
        if (isVisible) {
            redraw();
        }
    }
    
    /**
     * Invierte el orden de los bloques (taza con su tapa si existe), respetando los límites de tamaño.
     */
    public void reverseTower() {
        ArrayList<ArrayList<Elemento>> bloques = new ArrayList<ArrayList<Elemento>>();
        int i = 0;
        // Agrupar en bloques relacionados
        while (i < stack.size()) {
            ArrayList<Elemento> bloque = new ArrayList<Elemento>();
            Elemento actual = stack.get(i);
            if (actual instanceof Cup && i + 1 < stack.size()) {
                Elemento siguiente = stack.get(i + 1);
                if (siguiente instanceof Lid &&
                    siguiente.getNumber() == actual.getNumber()) {
                    bloque.add(actual);
                    bloque.add(siguiente);
                    bloques.add(bloque);
                    i = i + 2;
                    continue;
                }
            }
            bloque.add(actual);
            bloques.add(bloque);
            i = i + 1;
        }
        // Reconstruir en orden inverso
        ArrayList<Elemento> nueva = new ArrayList<Elemento>();
        int nuevaAltura = 0;
        for (int j = bloques.size() - 1; j >= 0; j--) {
            ArrayList<Elemento> bloque = bloques.get(j);
            int alturaBloque = 0;
            boolean cabe = true;
            for (int k = 0; k < bloque.size(); k++) {
                Elemento e = bloque.get(k);
                if (e.getWidth() > maxWidth) {
                    cabe = false;
                    break;
                }
                alturaBloque += e.getHeight();
            }
            if (cabe && nuevaAltura + alturaBloque <= maxHeight) {
                for (int k = 0; k < bloque.size(); k++) {
                    nueva.add(bloque.get(k));
                }
                nuevaAltura += alturaBloque;
            }
        }
        stack = nueva;
        currentHeight = nuevaAltura;
        ok(' ');
        if (isVisible) {
            redraw();
        }
    }
    
    /**
     * Retorna la altura actual de la torre
     */
    public int height() {
        return currentHeight;
    }
    
    public int[] liddedCups() {
        ArrayList<Integer> numeros = new ArrayList<Integer>();
        for (int i = 0; i < stack.size() - 1; i++) {
            Elemento actual = stack.get(i);
            Elemento siguiente = stack.get(i + 1);
            if (actual instanceof Cup && siguiente instanceof Lid && actual.getNumber() == siguiente.getNumber()) {
                numeros.add(actual.getNumber());
            }
        }

        for (int i = 0; i < numeros.size(); i++) {
            for (int j = i + 1; j < numeros.size(); j++) {
                if (numeros.get(j) < numeros.get(i)) {
                    int aux = numeros.get(i);
                    numeros.set(i, numeros.get(j));
                    numeros.set(j, aux);
                }
            }
        }
        int[] resultado = new int[numeros.size()];
        for (int i = 0; i < numeros.size(); i++) {
            resultado[i] = numeros.get(i);
        }
        ok(' ');
        return resultado;
    }
    
    /**
     * Retorna una representación de los elementos apilados en la torre.
     * Cada fila del arreglo contiene: [0]-> tipo de elemento ("cup" o "lid") y [1] -> número del elemento
     *
     * @return matriz con la información de los elementos en orden de apilamiento
     */
    public String[][] stackingItems() {
        // Crear matriz con tantas filas como elementos haya en la torre
        String[][] resultado = new String[stack.size()][2];
        // Recorrer la pila en el orden actual
        for (int i = 0; i < stack.size(); i++) {

            Elemento actual = stack.get(i);
            // Determinar el tipo de elemento

            if (actual instanceof Cup) {
                resultado[i][0] = "cup";
            } else {
                resultado[i][0] = "lid";
            }
            // Guardar el número del elemento como texto
            resultado[i][1] = String.valueOf(actual.getNumber());
        }
        ok(' ');
        return resultado;
    }
    
    /**
     * Tapa todas las tazas que tienen su tapa disponible en la torre
     * colocando la tapa inmediatamente encima de su taza correspondiente
     * Si la tapa ya está sobre la taza, no hace nada
     */
    public void cover() {
        boolean algoCambiou = false;
        boolean cambio = true;
        while (cambio) {
            cambio = false;
            for (int i = 0; i < stack.size(); i++) {
                Elemento actual = stack.get(i);
                if (actual instanceof Cup) {
                    int num = actual.getNumber();
                    // Buscar si ya tiene tapa encima
                    if (i + 1 < stack.size()) {
                        Elemento siguiente = stack.get(i + 1);
                        if (siguiente instanceof Lid && siguiente.getNumber() == num) {
                            continue; // Ya está tapada
                        }
                    }
                    // Buscar la tapa con el mismo número en otro lugar de la pila
                    int lidIdx = -1;
                    for (int j = 0; j < stack.size(); j++) {
                        if (j == i) continue;
                        Elemento e = stack.get(j);
                        if (e instanceof Lid && e.getNumber() == num) {
                            lidIdx = j;
                            break;
                        }
                    }
                    if (lidIdx != -1) {
                        // Mover la tapa justo encima de la taza
                        Elemento lid = stack.remove(lidIdx);
                        int newCupIdx = stack.indexOf(actual);
                        stack.add(newCupIdx + 1, lid);
                        cambio = true;
                        algoCambiou = true;
                        break;
                    }
                }
            }
        }
        ok(' ');
        if (isVisible) redraw();
    }
    
    /**
     * Hace visible la torre en el canvas.
     * Solo se permite si las dimensiones no superan 600x600.
     * Actualiza el estado de la operación.
     */
    public void makeVisible() {
        // Obtener instancia del canvas y activar visualización
        canvas = Canvas.getCanvas();
        isVisible = true;
        // Dibujar la torre actual
        redraw();
        ok(' ');
    }
    
    /**
     * Oculta la visualización de la torre.
     */
    public void makeInvisible()
    {
        // Si ya está invisible, no hacer nada
        if (!isVisible){
            ok('N');
            return;
        }
        canvas.setVisible(false);
        isVisible = false;
        ok(' ');
    }
    
    /**
     * Cierra completamente la aplicación.
     * Si la torre está visible, primero oculta el canvas.
     */
    public void exit() 
    {
        if (isVisible) {
            canvas.setVisible(false);
        }
        ok(' ');
        System.exit(0); 
    }
    
    /**
     * Encuentra el índice en la pila de un elemento por tipo y número.
     * @param type "cup" o "lid"
     * @param number número del elemento como String
     * @return índice en la pila, o -1 si no existe
     */
    private int findIndex(String type, String number) {
        int num;
        try {
            num = Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return -1;
        }
        for (int i = 0; i < stack.size(); i++) {
            Elemento e = stack.get(i);
            boolean tipoOk = (type.equals("cup") && e instanceof Cup) ||
                             (type.equals("lid") && e instanceof Lid);
            if (tipoOk && e.getNumber() == num) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Redibuja completamente la torre en el canvas, centrando cada elemento horizontalmente.
     */
    private void redraw() {
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        double escala = (double) canvasHeight / maxHeight;
        
        if (!isVisible) return;
        canvas.ClearAll();
        int y = canvasHeight;
        for (int i = 0; i < stack.size(); i++) {
            Elemento elementoActual = stack.get(i);
            // Posicionar desde la base hacia arriba
            int widthEscalado = (int)(elementoActual.getWidth() * escala);
            int heightEscalado = (int)(elementoActual.getHeight() * escala);
            y -= heightEscalado;
            Rectangle rectangulo = new Rectangle(widthEscalado,heightEscalado);
            Rectangle vacio = new Rectangle(widthEscalado-2,heightEscalado-1);
            // Centrar horizontalmente
            int posicionX = (canvasWidth - widthEscalado) / 2;
            rectangulo.setPosition(posicionX, y);
            vacio.setPosition(posicionX+1,y-1);
            visualElements.put(elementoActual, rectangulo);
            rectangulo.makeVisible("black");
            vacio.makeVisible("white");
        }
    }
    
    /**
     * Valida que el numero identificador de una tapa sea mayor o igual a 2
     * 
     * @param number numero a validar segun la subclase de elemento
     */
    public static int validateLid(int number)
    {
        if (number < 2) {
            ok('N');
            throw new IllegalArgumentException("Las tapas comienzan desde el numero 2.");
        }
        return number;
    }
    
    /**
     * Valida que el numero identificador de una taza sea mayor o igual a 1
     * 
     * @param number numero a validar segun la subclase de elemento
     */
    public static int validateCup(int number)
    {
        if (number < 1) {
            ok('N');
            throw new IllegalArgumentException("Las tazas comienzan desde el numero 1.");
        }
        return number;
    }
}
