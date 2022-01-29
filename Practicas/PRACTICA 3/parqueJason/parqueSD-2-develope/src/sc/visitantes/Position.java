package sc.visitantes;

public class Position {

	private Integer fila;

	private Integer col;

	public Position(Integer fila, Integer col) {
		this.fila = fila;
		this.col = col;
	}

	/**
	 * @return the fila
	 */
	public Integer getFila() {
		return fila;
	}

	/**
	 * @param fila the fila to set
	 */
	public void setFila(Integer fila) {
		this.fila = fila;
	}

	/**
	 * @return the col
	 */
	public Integer getCol() {
		return col;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((col == null) ? 0 : col.hashCode());
		result = prime * result + ((fila == null) ? 0 : fila.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (col == null) {
			if (other.col != null)
				return false;
		} else if (!col.equals(other.col))
			return false;
		if (fila == null) {
			if (other.fila != null)
				return false;
		} else if (!fila.equals(other.fila))
			return false;
		return true;
	}

	/**
	 * @param col the col to set
	 */
	public void setCol(Integer col) {
		this.col = col;
	}

	/**
	 * Sobrecarga del metod toString
	 */
	@Override
	public String toString() {
		return "" + this.fila + " " + this.col;
	}
}
