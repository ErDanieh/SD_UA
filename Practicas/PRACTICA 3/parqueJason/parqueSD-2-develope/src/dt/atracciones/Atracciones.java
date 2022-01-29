package dt.atracciones;

/**
 * Clase que representa los datos de una fila en la BD
 *
 */
public class Atracciones
{
	private Integer ID;
	private String Nombre;
	private Integer nTurno;
	private Integer nCola;
	private Integer tTurno;
	private Integer tEspera;
	private Integer posFila;
	private Integer posCol;

	public Atracciones(Integer ID, String Nombre, Integer nTurno, Integer nCola, Integer tTurno, Integer tEspera,
			Integer posFila, Integer posCol)
	{
		this.ID = ID;
		this.Nombre = Nombre;
		this.nTurno = nTurno;
		this.nCola = nCola;
		this.tTurno = tTurno;
		this.tEspera = tEspera;
		this.posFila = posFila;
		this.posCol = posCol;
	}

	public Atracciones(Atracciones aTr)
	{
		this.ID = aTr.ID;
		this.Nombre = aTr.Nombre;
		this.nTurno = aTr.nTurno;
		this.nCola = aTr.nCola;
		this.tTurno = aTr.tTurno;
		this.tEspera = aTr.tEspera;
		this.posFila = aTr.posFila;
		this.posCol = aTr.posCol;

	}

	@Override
	public String toString()
	{
		return "" + ID + " " + Nombre + " " + nTurno + " " + nCola + " " + tTurno + " " + tEspera + " " + tEspera + " ("
				+ posFila + ", " + posCol + ")";

	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		result = prime * result + ((Nombre == null) ? 0 : Nombre.hashCode());
		result = prime * result + ((nCola == null) ? 0 : nCola.hashCode());
		result = prime * result + ((nTurno == null) ? 0 : nTurno.hashCode());
		result = prime * result + ((posCol == null) ? 0 : posCol.hashCode());
		result = prime * result + ((posFila == null) ? 0 : posFila.hashCode());
		result = prime * result + ((tEspera == null) ? 0 : tEspera.hashCode());
		result = prime * result + ((tTurno == null) ? 0 : tTurno.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Atracciones other = (Atracciones) obj;
		if (ID == null)
		{
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;

		return true;
	}

	/**
	 * @return the iD
	 */
	public Integer getID()
	{
		return ID;
	}

	/**
	 * @param iD the iD to set
	 */
	public void setID(Integer iD)
	{
		ID = iD;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre()
	{
		return Nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre)
	{
		Nombre = nombre;
	}

	/**
	 * @return the nTurno
	 */
	public Integer getnTurno()
	{
		return nTurno;
	}

	/**
	 * @param nTurno the nTurno to set
	 */
	public void setnTurno(Integer nTurno)
	{
		this.nTurno = nTurno;
	}

	/**
	 * @return the nCola
	 */
	public Integer getnCola()
	{
		return nCola;
	}

	/**
	 * @param nCola the nCola to set
	 */
	public void setnCola(Integer nCola)
	{
		this.nCola = nCola;
	}

	/**
	 * @return the tTurno
	 */
	public Integer gettTurno()
	{
		return tTurno;
	}

	/**
	 * @param tTurno the tTurno to set
	 */
	public void settTurno(Integer tTurno)
	{
		this.tTurno = tTurno;
	}

	/**
	 * @return the tEspera
	 */
	public Integer gettEspera()
	{
		return tEspera;
	}

	/**
	 * @param tEspera the tEspera to set
	 */
	public void settEspera(Integer tEspera)
	{
		this.tEspera = tEspera;
	}

	/**
	 * @return the posFila
	 */
	public Integer getPosFila()
	{
		return posFila;
	}

	/**
	 * @param posFila the posFila to set
	 */
	public void setPosFila(Integer posFila)
	{
		this.posFila = posFila;
	}

	/**
	 * @return the posCol
	 */
	public Integer getPosCol()
	{
		return posCol;
	}

	/**
	 * @param posCol the posCol to set
	 */
	public void setPosCol(Integer posCol)
	{
		this.posCol = posCol;
	}
}
