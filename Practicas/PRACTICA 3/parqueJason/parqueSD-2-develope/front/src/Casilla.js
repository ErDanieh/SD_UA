
import './Casilla.css';

function Casilla(props) {

	const backGroundColor = props.backGroundColor


	return (
		<div className={props.className} style={{backgroundColor: backGroundColor}}>
			{props.value}
		</div>
	)
}


export default Casilla;