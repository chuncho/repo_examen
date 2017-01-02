import java.text.SimpleDateFormat
import test_solucion.FenomenoUtils

class BootStrap {
    def init = { servletContext ->
        FenomenoUtils.instance.cargainicial()
    }
    def destroy = {
    }


}
