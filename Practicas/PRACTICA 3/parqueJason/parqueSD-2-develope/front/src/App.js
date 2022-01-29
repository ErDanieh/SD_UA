import React from 'react';
import './App.css';
import './Mapa.css';

import Casilla from './Casilla.js';


function obtenerMapa(params) {

	const atracciones = this.obtenerAtracciones();
	const visitantes = obtenerVisitantes();
	const temperaturas = obtenerTemperaturas();


	return {
		atracciones,
		visitantes,
		temperaturas
	}
}



function obtenerVisitantes() {

}

function obtenerTemperaturas() {

}

class App extends React.Component {



	constructor(props) {
		super(props);
		this.state = {
			mapaSD: [
				1, null, 3, 4, 2, 2, 5, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
				1, 22, 3, 4, 2, 2, 5, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
				1, 2, 3, 4, 2, 2, 5, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
				1, 2, 3, 4, 2, 2, 5, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
				1, 2, 3, 4, 2, 2, 5, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
				1, 2, 3, 4, 2, 2, 5, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
				1, 2, 3, 4, 2, 2, 5, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
				1, 2, 3, 4, 2, 2, 5, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
				1, 2, 3, 4, 2, 2, 5, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
				1, 2, 3, 4, 2, 2, 5, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,

				1, 2, 3, 4, 2, 2, 5, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
				1, 2, 3, 4, 2, 2, 5, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
				1, 2, 3, 4, 2, 2, 5, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
				1, 2, 3, 4, 2, 2, 5, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
				1, 2, 3, 4, 2, 2, 5, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
				1, 2, 3, 4, 2, 2, 5, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
				1, 2, 3, 4, 2, 2, 5, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
				1, 2, 3, 4, 2, 2, 5, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
				1, 2, 3, 4, 2, 2, 5, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
				1, 2, 3, 4, 2, 2, 5, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,

			],
			numVisitantes: 2,
			temperaturas: [20, 20, 20, 20]
		};
	}

	updateTemperaturas() {
		var temperaturasFetch = [20, 20, 20, 20]

		var requestOptions = {
			method: 'GET',
			redirect: 'follow'
		};

		fetch("http://localhost:3010/temperaturas", requestOptions)
			.then(response => response.json())
			.then(temperaturas => {
				temperaturas.forEach(element => {
					let pos = element['cuadrante'] - 1;
					temperaturasFetch[pos] = element['temperatura'];
				});
			})
			.then(() => this.setState({
				...this.state,
				temperaturas: temperaturasFetch
			}))
			.catch(error => {
				alert('Conexion con la API perdida')
				console.log('error al fetchear');
				this.setState({
					...this.state,
					temperaturas: temperaturasFetch
				})
			});
	}

	updateMapa() {
		var mapa =
			[
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,

				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
			];

		var requestOptions = {
			method: 'GET',
			redirect: 'follow'
		};

		fetch("http://localhost:3010/atracciones", requestOptions)
			.then(response => response.json())
			.then(atraccion => {
				atraccion.forEach(element => {
					let posRelativa = element['posFila'] + element['posColumna'] * 20;
					console.log(posRelativa)
					if (element['tEspera'] === 70000) {
						mapa[posRelativa] = 'X';
					}
					else {
						mapa[posRelativa] = element['tEspera'];
					}
					console.log(this.state);
					return mapa;

				})
			})
			.then(() => this.setState({
				...this.state,
				mapaSD: mapa
			}))
			.catch(error => {
				//alert('Conexion con la API perdida')
				console.log('error al fetchear');
				this.setState({
					...this.state,
					mapaSD: mapa
				})
			});


		fetch("http://localhost:3010/visitantes", requestOptions)
			.then(response => response.json())
			.then(visitantes => {
				visitantes.forEach(element => {

					if (element['posFila'] !== null && element['posColumna'] !== null) {
						let posRelativa = element['posFila'] + element['posColumna'] * 20;
						console.log(posRelativa)
						mapa[posRelativa] = element['ID'];
					}

					console.log(this.state);
					return mapa;

				})
			})
			.then(() => this.setState({
				...this.state,
				mapaSD: mapa
			}))
			.catch(error => {
				//alert('Conexion con la API perdida')
				console.log('error al fetchear');
				this.setState({
					...this.state,
					mapaSD: mapa
				})
			});

	}

	componentDidMount() {
		this.interval = setInterval(() => {
			this.updateMapa();
			this.updateTemperaturas();
		}, 1000);
	}


	componentWillUnmount() {
		clearInterval(this.interval);
	}





	render() {
		console.log('---');
		console.log(this.state);
		console.log('---');

		return (
			<div className="App">
				<h1>PARQUE SD</h1>
				<div className="mapaGrid">
					{this.state.mapaSD.map((valor, index) => {

						const indTrunc = Math.trunc(index / 10);

						if (index < 200 && indTrunc % 2 === 0) {
							return <Casilla value={null} value={valor} className='casilla' backGroundColor='rgb(200, 245, 200)' />
						}
						else if (index < 200 && indTrunc % 2 !== 0) {
							return <Casilla value={null} value={valor} className='casilla' backGroundColor='rgb(255, 248, 239)' />
						}
						else if (index >= 200 && indTrunc % 2 === 0) {
							return <Casilla value={null} value={valor} className='casilla' backGroundColor='rgb(196, 196, 253)' />
						}
						else
							return <Casilla value={null} value={valor} className='casilla' backGroundColor='rgb(255, 175, 175)' />
					}
					)}
				</div>

				<h2 className='tituloTemperatura'>Temperaturas</h2>

				<div className='leyendaValor'>
					
					<div className='elementoLeyenda'>
						<Casilla  className='casilla' backGroundColor='rgb(200, 245, 200)' />
						<p>{this.state.temperaturas[0]}º</p>
					</div>
					<div className='elementoLeyenda'>
						<Casilla  className='casilla' backGroundColor='rgb(255, 248, 239)' />
						<p>{this.state.temperaturas[2]}º</p>
					</div>
					<div className='elementoLeyenda'>
						<Casilla  className='casilla' backGroundColor='rgb(196, 196, 253)' />
						<p>{this.state.temperaturas[1]}º</p>
					</div>
					<div className='elementoLeyenda'>
						<Casilla  className='casilla' backGroundColor='rgb(255, 175, 175)' />
						<p>{this.state.temperaturas[3]}º</p>
					</div>
				</div>

			</div>
		);
	}
}

export default App;
