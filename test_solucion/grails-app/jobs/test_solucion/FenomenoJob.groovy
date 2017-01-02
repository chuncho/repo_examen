package test_solucion

import java.text.SimpleDateFormat

/**
 * Created by Francisco on 27/12/2016.
 */
class FenomenoJob {

    def fenomenoService

    static triggers = {
        cron cronExpression: "0 1 0 1/1 * ? *"
    }

    def execute(){
        Fenomeno nuevoFenomeno = new Fenomeno()
        nuevoFenomeno = fenomenoService.obtenerPrimero()
        fenomenoService.eliminar(nuevoFenomeno)
        nuevoFenomeno = new Fenomeno()
        nuevoFenomeno = fenomenoService.obtenerUltimo()
        def respuesta = FenomenoUtils.instance.incrementoDia(nuevoFenomeno.fecha, nuevoFenomeno.gBetasoide, nuevoFenomeno.gFarengi, nuevoFenomeno.gVulcano)

        Integer gBetasoide = respuesta.Betasoide as int
        Integer gFarengi = respuesta.Farengi as int
        Integer gVulcano = respuesta.Vulcano as int
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd")
        Date fecha = dt.parse(respuesta.Fecha)

        String vPeriodo = FenomenoUtils.instance.evaluarUbicacion(gBetasoide, gFarengi, gVulcano)
        fenomenoService.create(fecha, vPeriodo, gBetasoide, gFarengi, gVulcano)

    }
}
