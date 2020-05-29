package com.example.backend.rest.type.response;

import java.util.List;

import com.example.backend.rest.type.base.ResponseBase;
import com.example.backend.rest.type.business.PlanesSuscripcionRest;


public class ConsultaPlanesSuscripcionResponse extends ResponseBase {

    private List<PlanesSuscripcionRest> planes;

	public List<PlanesSuscripcionRest> getPlanes() {
		return planes;
	}
	public void setPlanes(List<PlanesSuscripcionRest> planes) {
		this.planes = planes;
    }
    
}