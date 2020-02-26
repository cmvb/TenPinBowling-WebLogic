package co.com.cmvb.tpb.tenpinbowling.util;

import co.com.cmvb.tpb.tenpinbowling.configuracion.CoreException;
import co.com.cmvb.tpb.tenpinbowling.constantes.ConstantesValidaciones;
import co.com.cmvb.tpb.tenpinbowling.enums.ESeveridadMensaje;
import co.com.cmvb.tpb.tenpinbowling.enums.excepcion.EMensajeException;
import co.com.cmvb.tpb.tenpinbowling.mb.SesionMB;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.primefaces.PrimeFaces;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.shaded.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Util {

    private static final Logger LOG = Logger.getLogger(Util.class.getName());

    public static final String NOMBRE_LLAVE_TOKEN = "app.bean.sesion.tpb";
    private static final String CARACTERES = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int TAMANO_TOKEN = 11;
    private static final char[] BUFFER = new char[TAMANO_TOKEN];
    public static final int ELEMENTOCERO = 0;
    public static final String VACIO = "";
    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    public static final String FORMATO_FECHA_INFORMES = "DD.MM.YYYY:HH24:MI:SS";
    public static final String CARGUE_EXITOSO = "Archivo procesado correctamente";

    public static final String INFORMACION = "Información";
    public static final String ADVERTENCIA = "Advertencia";
    public static final String ERROR = "Error";

    public static final String CAMPO_REQUERIDO = "Campo requerido";
    public static final String VALOR_INCORRECTO = "Valor incorrecto";
    public static final String VALOR_VACIO = "Está vacío";
    public static final String VALOR_EXISTENTE = "Valor existente";

    public static final int CANTIDAD_REGISTROS_TABLA_MOSTRAR = 5;
    public static final int CANTIDAD_REGISTROS_TABLA_MOSTRAR_X10 = 50;
    public static final String ESTADO_ACTIVO_TEXTO = "A";
    public static final String ESTADO_INACTIVO_TEXTO = "I";
    public static final String STRINGVACIO = " ";

    public static final String END_LINE = "\r\n";

    public static final String EXTENSION_TXT = "txt";
    public static final String EXTENSION_CSV = "csv";
    public static final String EXTENSION_XLSX = "xlsx";
    public static final String EXTENSION_XLS = "xls";

    /**
     * Borra todos los mensajes en el contexto de la peticion.
     */
    public static final void borrarMensajes() {
        FacesContext contexto = FacesContext.getCurrentInstance();
        Iterator<FacesMessage> it = contexto.getMessages();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    /**
     * Permite agregar mensajes en el contexto de la peticion.
     *
     * @param id del tag donde quiere que se muestre el mensaje
     * @param mensaje que se mostrara en pantalla
     */
    public static final void agregarMensajePorId(String id, String mensaje) {
        FacesContext.getCurrentInstance().addMessage(id, new FacesMessage(mensaje));
    }

    /**
     * Crea un mensaje global mediante un contexto.
     *
     * @param mensaje
     */
    public static final void agregarMensajeGlobales(String mensaje) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, StringUtils.EMPTY));
    }

    /**
     *
     * Permite subir una variable a la sesion de Usuario
     *
     * @param id
     * @param obj
     *
     */
    public static void subirVariableSesion(String id, Object obj) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(id, obj);
    }

    /**
     *
     * Permite Retornar una variable en la sesion de Usuario
     *
     * @param id
     * @return Object
     *
     */
    public static Object bajarVariableSesion(String id) {
        return (Object) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(id);
    }

    /**
     * recupera el valor de la llave en el mapa de sesión
     *
     * @param llave
     * @return
     */
    public static Object getAtributoSesion(String llave) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(llave) == null ? "" : FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(llave);
    }

    /**
     *
     * Permite Guardar Notificaciones en la sesion de usuario
     *
     * @param msj
     * @param severidad
     *
     */
    public static void guardarNotificacionUsuarioSesion(String msj, String severidad) {
        try {
            SesionMB sesion = (SesionMB) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Util.NOMBRE_LLAVE_TOKEN);
            sesion.setClaseMsj(severidad);
            sesion.setMsj(msj);
            sesion.setTituloMsj(Util.getTituloNotificacion(severidad));

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(NOMBRE_LLAVE_TOKEN, sesion);
        } catch (Exception ex) {
            String errorTraza = Arrays.toString(ex.getStackTrace());
            System.out.println(errorTraza);
        }
    }

    /**
     *
     * Permite Retornar la clase o severidad para notificaciones y mensajes
     *
     * @param severidad
     * @return
     */
    public static String getSeveridadMensaje(ESeveridadMensaje severidad) {
        return severidad.getNombre();
    }

    /**
     *
     * Permite Retornar El titulo para los mensajes notificaciones de acuerdo a
     * la clase bootstrap
     *
     * @param claseMsj
     * @return String
     *
     */
    public static String getTituloNotificacion(String claseMsj) {
        String titulo = Util.INFORMACION;

        if (claseMsj.equalsIgnoreCase(ESeveridadMensaje.WARNING.getNombre())) {
            titulo = Util.ADVERTENCIA;
        } else if (claseMsj.equalsIgnoreCase(ESeveridadMensaje.DANGER.getNombre())) {
            titulo = Util.ERROR;
        }

        return titulo;
    }

    /**
     *
     * Traer el objeto SesionMB de la Sesion Actual
     *
     * @return
     *
     */
    public static SesionMB obtenerSesionActual() {
        SesionMB sesion = (SesionMB) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Util.NOMBRE_LLAVE_TOKEN);
        if (sesion.getSesion() == null) {
            PrimeFaces.current().executeScript("irLoginGeneral()");
        }

        return sesion;
    }

    /**
     * valida si la cadena es numero
     *
     * @param cadena
     * @return
     */
    public static boolean esNumero(String cadena) {
        return cadena.matches("[0-9]*");
    }

    /**
     * Exportar archivo extension CSV
     *
     * @param nombreArchivo
     * @param cabecera
     * @param rows
     * @throws co.com.cmvb.tpb.tenpinbowling.configuracion.CoreException
     */
    public static void exportarArchivoCSV(String nombreArchivo, String cabecera, List<String> rows) throws CoreException {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();

            response.reset();
            response.setContentType("text/comma-separated-values");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + nombreArchivo + ".csv\"");

            OutputStream output = response.getOutputStream();
            if (!StringUtils.isBlank(cabecera)) {
                output.write((cabecera + END_LINE).getBytes());
            }
            for (String s : rows) {
                output.write((s + END_LINE).getBytes());
            }

            output.flush();
            output.close();

            fc.responseComplete();
        } catch (IOException ex) {
            throw new CoreException(EMensajeException.getMensaje(ex), ex.getCause());
        }
    }

    /**
     * Lee un fichero y lo guarda en un String
     *
     * @param rutaArchivo ruta del archivo
     * @return archivo leido en un string
     * @throws java.io.FileNotFoundException
     */
    public static String obtenerFicheroString(String rutaArchivo) throws FileNotFoundException, IOException {
        String file = "";

        String cadena;
        FileReader f = new FileReader(rutaArchivo);
        BufferedReader b = new BufferedReader(f);

        while ((cadena = b.readLine()) != null) {
            System.out.println(cadena);
            file = file + cadena;
        }
        b.close();

        return file;
    }

    /**
     * Valida caracteres especiales
     *
     * @param cadenaValidar
     * @return
     */
    public static boolean validarCaracteres(String cadenaValidar) {
        Pattern mask = Pattern.compile(ConstantesValidaciones.EXPRESION_REGULAR_DE_TEXTO_INGRESADO);
        return mask.matcher(cadenaValidar).matches();
    }

    /**
     * valida que la cadena solo tenga letras
     *
     * @param cadena
     * @return
     */
    public static boolean validarSoloTexto(String cadena) {
        Pattern mask = Pattern.compile(ConstantesValidaciones.EXPRESION_REGULAR_SOLO_TEXTO_INGRESADO);
        return mask.matcher(cadena).matches();
    }

    /**
     * Valida Direcciones
     *
     * @param cadenaValidar
     * @return
     */
    public static boolean validarDirecciones(String cadenaValidar) {
        Pattern mask = Pattern.compile(ConstantesValidaciones.EXPRESION_REGULAR_DE_DIRECCIONES);
        return mask.matcher(cadenaValidar).matches();
    }

    /**
     * redirecciona a la pagina 500 cuando se produce una excepción
     *
     * @param ex
     */
    public static void enviarPantallaException(CoreException ex) {
        Util.guardarNotificacionUsuarioSesion(null, Util.getSeveridadMensaje(ESeveridadMensaje.DEFAULT));
        String titleMensaje = Util.obtenerSesionActual().getMensajeExcepcion();
        titleMensaje = titleMensaje.replaceAll("\n", "").replaceAll("\r", "").replaceAll("\"", "").replaceAll("\'", "");

        List<StackTraceElement> listaExc = Arrays.asList(ex.getStackTrace());
        List<String> listaCause = new ArrayList<>();

        StringBuilder error = new StringBuilder();
        error.append("EXCEPCIÓN CAUSADA POR: ").append("</br>");
        error.append("---------------------- ").append("</br>");
        error.append("</br>");
        if (ex.getCause() != null) {
            listaCause.addAll(Arrays.asList(ex.getCause().toString().split(" ")));
        }
        for (String element : listaCause) {
            error.append(element).append(" ");
        }
        error.append("</br>").append("</br>");
        error.append("PILA DEL ERROR: ").append("</br>");
        error.append("--------------- ").append("</br>");
        error.append("</br>");
        for (StackTraceElement element : listaExc) {
            error.append(element.toString()).append("</br>");
        }

        String error500 = error.toString().replaceAll("\n", "").replaceAll("\r", "").replaceAll("\"", "").replaceAll("\'", "");

        PrimeFaces.current().executeScript("localStorage.setItem('error500Title', '" + titleMensaje + "')");
        PrimeFaces.current().executeScript("localStorage.setItem('error500', '" + error500 + "')");
        PrimeFaces.current().executeScript("$('.page-content--bge5').hide();");
        PrimeFaces.current().executeScript("irError500()");
    }

    public static Double formatearDecimales(Double numero, Integer numeroDecimales) {
        return Math.round(numero * Math.pow(10, numeroDecimales)) / Math.pow(10, numeroDecimales);
    }

    /**
     * Exportar archivo dada su ruta exacta
     *
     * @param ruta
     * @throws java.lang.Exception
     */
    public static void exportarArchivo(String ruta) throws Exception {
        try {
            File f = new File(ruta);

            String nombreArchivo = f.getName();

            //se crean los objetos para enviar la petición de descarga
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
            response.reset();
            response.setContentType("text/comma-separated-values");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + nombreArchivo);
            //objeto para escribir el archivo
            ServletOutputStream out = response.getOutputStream();

            //OutputStream output = new FileOutputStream(nombreArchivo);
            byte[] buf = new byte[8192];
            FileInputStream is = new FileInputStream(f);

            int c = 0;

            while ((c = is.read(buf, 0, buf.length)) > 0) {
                out.write(buf, 0, c);
                out.flush();
            }

            out.flush();
            out.close();

            fc.responseComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * método que valida la cantidad máxima de caracteres permitido para el
     * campo descripción
     *
     * @param texto
     * @param tamano
     * @return
     */
    public static boolean esValidoTamanoTexto(String texto, int tamano) {
        return texto != null ? texto.length() <= tamano : false;
    }

    /**
     * Permite validar la extension del archivo que se va a importar.
     *
     * @param nombreArchivo
     * @param extension
     * @return true si la extension es .csv de lo contrario false.
     */
    public static boolean validarExtensionArchivo(String nombreArchivo, String extension) {
        boolean resultado = true;

        if (!extension.equalsIgnoreCase(Util.extensionArchivo(nombreArchivo))) {
            resultado = false;
        }
        return resultado;
    }

    /**
     * Permite validar en contenido de una cadena de caracteres
     *
     * @param cadena
     * @return true si la cadena cumple las validaciones
     */
    public static boolean vacio(String cadena) {
        return cadena == null || cadena.length() == 0 || cadena.equals("null") || cadena.trim().length() == 0;
    }

    /**
     * Devuelve solo la extension del archivo.
     *
     * @param nombreArchivoConExtension recibe el nombre del archivo con la
     * extension
     * @return nombre de la extension
     */
    public static final String extensionArchivo(String nombreArchivoConExtension) {
        String extension = FilenameUtils.getExtension(nombreArchivoConExtension);
        return extension;
    }

    /**
     * Metodo que transforma un texto en entidad XML Document
     *
     * @param xmlStr es un String que contiene el xml en texto
     * @return Document entidad XML
     * @throws co.com.cmvb.tpb.tenpinbowling.configuracion.CoreException
     */
    public static Document convertStringToDocument(String xmlStr) throws CoreException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
            return doc;
        } catch (IOException | ParserConfigurationException | SAXException ex) {
            LOG.warning(ex.getMessage());
            throw new CoreException(EMensajeException.getMensaje(ex), ex.getCause());
        }
    }

    /**
     * Permite leer el contenido de un InputStream
     *
     * @param file archivo que se quiere leer extension
     * @return
     * @throws co.com.cmvb.tpb.tenpinbowling.configuracion.CoreException
     */
    public static final List<String> listaCargueArchivos(InputStream file) throws CoreException {
        List<String> result = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(file));

            // Lectura del fichero por líneas
            String linea;
            while ((linea = br.readLine()) != null) {
                result.add(linea);
            }
        } catch (IOException ex) {
            LOG.warning(ex.getMessage());
            throw new CoreException(EMensajeException.getMensaje(ex), ex.getCause());
        }

        return result;
    }
}
