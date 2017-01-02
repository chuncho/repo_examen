package test_solucion

import grails.transaction.Transactional

@Transactional
class FenomenoService {

    def create(pfecha, pPeriodo, pgBetasoide, pgFarengi, pgVulcano) {
        Fenomeno nuevoFenomeno = new Fenomeno(fecha: pfecha, gBetasoide: pgBetasoide, periodo: pPeriodo,
                gFarengi: pgFarengi, gVulcano: pgVulcano)
        nuevoFenomeno.save(failOnError: true)
        nuevoFenomeno
    }

    def Fenomeno consulta(long dia) {
        Fenomeno nuevoFenomeno = new Fenomeno()
        nuevoFenomeno = Fenomeno.findById(dia)
        nuevoFenomeno
    }

    def Fenomeno obtenerPrimero() {
        Fenomeno nuevoFenomeno = new Fenomeno()
        nuevoFenomeno = Fenomeno.first("id")
        nuevoFenomeno
    }

    def Fenomeno obtenerUltimo() {
        Fenomeno nuevoFenomeno = new Fenomeno()
        nuevoFenomeno = Fenomeno.last("id")
        nuevoFenomeno
    }

    def ArrayList<Fenomeno> listadoFenomenos() {
        List listaFenomenos = new ArrayList<Fenomeno>()
        listaFenomenos = Fenomeno.withCriteria {
            order("id", "asc")
        }
        listaFenomenos
    }

    def eliminar(Fenomeno fenomeno){
        fenomeno.delete()
    }


}
