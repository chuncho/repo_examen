package test_solucion

import grails.converters.JSON

import java.text.SimpleDateFormat

class FenomenoController {

    def fenomenoService

    def index(){

        String vPeriodoAnterior = ""
        Integer vPeriodoLluvia = 0
        Integer vPeriodoSequia = 0
        Integer vPeriodoOptimo = 0
        Float vAreaMaxima = 0
        Float vAreaFenomeno = 0
        Long vDiaMaxLluvia = 0
        Date vFechaMaxLuvia = new Date()

        List listaFenomenos = new ArrayList<Fenomeno>()
        listaFenomenos = fenomenoService.listadoFenomenos()
        Fenomeno menorFenomeno = new Fenomeno()
        menorFenomeno = fenomenoService.obtenerPrimero()

        for(Fenomeno fenomeno :listaFenomenos){
            if(fenomeno.periodo.equals("L")){
                vAreaFenomeno = FenomenoUtils.instance.calculoAreaParticular(fenomeno)
                if(vAreaMaxima < vAreaFenomeno){
                    vAreaMaxima = vAreaFenomeno
                    vFechaMaxLuvia = fenomeno.fecha
                    vDiaMaxLluvia = fenomeno.id - (menorFenomeno.id - 1)
                }
            }
            if((!fenomeno.periodo.equals("X")) && (!fenomeno.periodo.equals(vPeriodoAnterior))){
                if(fenomeno.periodo.equals("L")){
                    vPeriodoLluvia ++
                }
                if(fenomeno.periodo.equals("S")){
                    vPeriodoSequia ++
                }
                if(fenomeno.periodo.equals("O")){
                    vPeriodoOptimo ++
                }
            }
            vPeriodoAnterior = fenomeno.periodo
        }
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd")
        String fechaFormat = dt.format(vFechaMaxLuvia)

        render(view:"index", model:[periodolluvia : vPeriodoLluvia, periodosequia : vPeriodoSequia, periodooptimo : vPeriodoOptimo, fechamaxlluvia : fechaFormat, diamaxlluvia : vDiaMaxLluvia])
    }

    def list(){
        List listaFenomenos = new ArrayList<Fenomeno>()
        listaFenomenos = fenomenoService.listadoFenomenos()
        render(view:"list", model:[lFenomenos : listaFenomenos])
    }

    def clima(){
        def response = [:]
        Fenomeno nuevoFenomeno = new Fenomeno()
        Fenomeno menorFenomeno = new Fenomeno()
        try{
            Long dia = params.dia as Long
            response.dia = dia
            menorFenomeno = fenomenoService.obtenerPrimero()
            dia = dia + (menorFenomeno.id -1)
            nuevoFenomeno = fenomenoService.consulta(dia)

            if(nuevoFenomeno.periodo == "L"){
                response.clima = "LLUVIA"
            }
            if(nuevoFenomeno.periodo == "S"){
                response.clima = "SEQUIA"
            }
            if(nuevoFenomeno.periodo == "O"){
                response.clima = "OPTIMO"
            }
            if(nuevoFenomeno.periodo == "X"){
                response.clima = "INDEFINIDO"
            }
        }catch (Exception e){
            response.estado = "ERROR"
            response.message = "Error en consulta"
            response.remove("dia")
        }
        render( response as JSON )
    }

}
