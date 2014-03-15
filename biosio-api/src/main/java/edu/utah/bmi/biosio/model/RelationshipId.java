/*
 * iBIOMES - Integrated Biomolecular Simulations
 * Copyright (C) 2014  Julien Thibault, University of Utah
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.utah.bmi.biosio.model;

import java.io.Serializable;

public class RelationshipId implements Serializable {
	
	private static final long serialVersionUID = -1827233381693481597L;
	
	protected String conceptFrom;
    protected String conceptTo;
    protected String relationshipDefinition;
    
    public RelationshipId(){
    }
    
    public RelationshipId(String conceptFrom, String conceptTo, String relationshipDefinitionID){
    	this.conceptFrom = conceptFrom;
    	this.conceptTo = conceptTo;
    	this.relationshipDefinition = relationshipDefinitionID;
    }

	public String getConceptFrom() {return conceptFrom;}
	public void setConceptFrom(String conceptFrom) {this.conceptFrom = conceptFrom;}

	public String getConceptTo() {return conceptTo;}
	public void setConceptTo(String conceptTo) {this.conceptTo = conceptTo;}

	public String getRelationshipDefinition() {return relationshipDefinition;}
	public void setRelationshipDefinition(String relationshipDefinition) {this.relationshipDefinition = relationshipDefinition;}
	
	
	@Override
    public boolean equals(Object obj) {
        if(obj instanceof RelationshipId){
        	RelationshipId pk = (RelationshipId) obj;
            if(!pk.getConceptFrom().equals(conceptFrom)){
                return false;
            }
            if(!pk.getConceptTo().equals(conceptTo)){
                return false;
            }
            if(!pk.getRelationshipDefinition().equals(relationshipDefinition)){
                return false;
            }
            return true;
        }
        return false;
	}

	@Override
	public int hashCode() {
		return conceptFrom.hashCode() + relationshipDefinition.hashCode() + conceptTo.hashCode();
	}
}
