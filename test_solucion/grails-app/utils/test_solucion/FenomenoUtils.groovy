package test_solucion

import grails.util.Holders

import java.text.DecimalFormat
import java.text.SimpleDateFormat

/**
 * Created by Francisco on 27/12/2016.
 */
@Singleton
class FenomenoUtils {

    def fenomenoService

    public cargainicial(){
        this.fenomenoService = Holders.applicationContext.getBean("fenomenoService")

        Date fechaHoy = new Date()
        Date fechaFin = new Date()
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd")

        Calendar calendar = Calendar.getInstance()
        calendar.setTime(fechaFin)
        calendar.add(Calendar.YEAR, 10)
        fechaFin = calendar.getTime()

        Long diff = fechaFin.getTime() - fechaHoy.getTime()
        Integer dias = (diff / (1000 * 60 * 60 * 24))

        Integer gBetasoide = 0
        Integer gFarengi = 0
        Integer gVulcano = 0
        String vPeriodo
        Date fecha = new Date()
        for(int i = 1; i < dias + 1; i++){
            def respuesta = incrementoDia(fecha, gBetasoide, gFarengi, gVulcano)

            gBetasoide = respuesta.Betasoide as int
            gFarengi = respuesta.Farengi as int
            gVulcano = respuesta.Vulcano as int
            fecha = dt.parse(respuesta.Fecha)

            vPeriodo = evaluarUbicacion(gBetasoide, gFarengi, gVulcano)
            fenomenoService.create(fecha, vPeriodo, gBetasoide, gFarengi, gVulcano)
        }

    }

    public Object incrementoDia(Date fecha, int gBetasoide, int gFarengi, int gVulcano){ // incrementa el angulo de rotacion del planeta y el dia

        def respuesta = [ Fecha : null, Betasoide : 0, Farengi : 0, Vulcano : 0 ]

        if (gFarengi == 0){
            gFarengi = 359
        }else{
            gFarengi --
        }

        switch (gBetasoide) {
            case 0: gBetasoide = 357; break;
            case 1: gBetasoide = 358; break;
            case 2: gBetasoide = 359; break;
            default: gBetasoide = gBetasoide - 3;
        }

        switch (gVulcano) {
            case 355: gVulcano = 0; break;
            case 356: gVulcano = 1; break;
            case 357: gVulcano = 2; break;
            case 358: gVulcano = 3; break;
            case 359: gVulcano = 4; break;
            default: gVulcano = gVulcano + 5;
        }

        Calendar cal = Calendar.getInstance()
        cal.setTime(fecha)
        cal.add(Calendar.DATE, 1)

        respuesta.Farengi = gFarengi
        respuesta.Betasoide = gBetasoide
        respuesta.Vulcano = gVulcano
        respuesta.Fecha = cal.getTime().format("yyyy-MM-dd")

        respuesta
    }

    public Float obtenerPendiente(Float xPa, Float yPa, Float xPb, Float yPb){ // dados dos puntos en formato cartesiano (X,Y) devuelve la pendiente

        Float vPendiente
        Float vNumerador
        Float vDenominador
        vNumerador = (yPb - yPa)
        vDenominador = (xPb - xPa)

        if(Math.abs(vDenominador) != 0){
            vPendiente = vNumerador / vDenominador
        }else{
            vPendiente = 0
        }
        vPendiente = Math.abs(vPendiente)
        vPendiente
    }

    public Float obtenerArea(Float xPa, Float yPa, Float xPb, Float yPb, Float xPc, Float yPc){ // dados 3 puntos en formato cartesiano (X,Y) devuelve el area del triangulo
        Float vMinuendo = (((xPa)*(yPb))+((xPb)*(yPc))+((xPc)*(yPa)))
        Float vSustraendo = (((xPa)*(yPc))+((xPc)*(yPb))+((xPb)*(yPa)))
        Float vArea = ((vMinuendo - (vSustraendo))/2)
        vArea = Math.abs(vArea)
        vArea
    }

    public String evaluarUbicacion(Integer gBetasoide, Integer gFarengi, Integer gVulcano){ // Enviando objeto con posicion de planetas, evalua si es un dia con clima lluvioso ( vPeriodo = 'L' )
        String retorno = 'X'

        Integer rBetasoide = 2000
        Integer rFarengi = 500
        Integer rVulcano = 1000

        Float xBetasoide = obtenerX(rBetasoide,gBetasoide)
        Float yBetasoide = obtenerY(rBetasoide,gBetasoide)
        Float xVulcano = obtenerX(rVulcano,gVulcano)
        Float yVulcano = obtenerY(rVulcano,gVulcano)
        Float xFarengi = obtenerX(rFarengi,gFarengi)
        Float yFarengi = obtenerY(rFarengi,gFarengi)

        if((obtenerPendiente(xBetasoide, yBetasoide, xFarengi, yFarengi) == obtenerPendiente(xFarengi, yFarengi, xVulcano, yVulcano)) &&
                (obtenerPendiente(xBetasoide, yBetasoide, xFarengi, yFarengi) == obtenerPendiente(xBetasoide, yBetasoide, xVulcano, yVulcano))) {

            retorno = 'O'

            if (obtenerPendiente(xBetasoide, yBetasoide, xFarengi, yFarengi) == obtenerPendiente(xBetasoide, yBetasoide, 0, 0)) {
                retorno = 'S'
            }

        }else{
            Float areaBVF = obtenerArea(xBetasoide, yBetasoide, xVulcano, yVulcano, xFarengi, yFarengi)
            Float areaBVC = obtenerArea(xBetasoide, yBetasoide, xVulcano, yVulcano, 0, 0)
            Float areaBCF = obtenerArea(xBetasoide, yBetasoide, 0, 0, xFarengi, yFarengi)
            Float areaCVF = obtenerArea(0, 0, xVulcano, yVulcano, xFarengi, yFarengi)
            Float vSum = areaBVC + areaBCF + areaCVF


            if(areaBVF != 0){
                if(areaBVF == vSum){
                    retorno = 'L'
                }
            }
        }

        retorno
    }

    public Float calculoAreaParticular(Fenomeno fenomeno){

        Integer rBetasoide = 2000
        Integer rFarengi = 5000
        Integer rVulcano = 1000

        Float xBetasoide = obtenerX(rBetasoide,fenomeno.gBetasoide)
        Float yBetasoide = obtenerY(rBetasoide,fenomeno.gBetasoide)
        Float xVulcano = obtenerX(rVulcano,fenomeno.gVulcano)
        Float yVulcano = obtenerY(rVulcano,fenomeno.gVulcano)
        Float xFarengi = obtenerX(rFarengi,fenomeno.gFarengi)
        Float yFarengi = obtenerY(rFarengi,fenomeno.gFarengi)

        Float areaBVF = obtenerArea(xBetasoide, yBetasoide, xVulcano, yVulcano, xFarengi, yFarengi)
        areaBVF
    }

    public Float obtenerX(int radio, int angulo){
        Double anguloRadianes = Math.toRadians(angulo)
        Double coseno = Math.cos(anguloRadianes)
        Float puntoX = (radio * coseno.round(2))
        puntoX
    }

    public Float obtenerY(int radio, int angulo){
        Double anguloRadianes = Math.toRadians(angulo)
        Double seno = Math.sin(anguloRadianes)
        Float puntoY = (radio * seno.round(2))
        puntoY
    }

}
