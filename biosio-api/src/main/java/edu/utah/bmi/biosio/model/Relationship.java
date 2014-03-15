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

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

/**
 * Relationship between 2 concepts
 * @author Julien Thibault, University of Utah
 *
 */
@Entity
@Table(name="RELATIONSHIP")
public class Relationship
{
	/*@EmbeddedId
	private RelationshipId id;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@MapsId("cui1")
	@JoinColumn(name="cui1", referencedColumnName="cui")
	private Concept conceptFrom;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@MapsId("cui2")
	@JoinColumn(name="cui2", referencedColumnName="cui")
	private Concept conceptTo;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@MapsId("rel_id")
	@JoinColumn(name="rel_id", referencedColumnName="cui")
	private Concept relationshipDefinition;*/

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
	private Long id;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="cui1", referencedColumnName="cui")
	private Concept conceptFrom;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="cui2", referencedColumnName="cui")
	private Concept conceptTo;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="rel_id", referencedColumnName="cui")
	private Concept relationshipDefinition;
	
	public Relationship(){
	}

	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}
	
	public Concept getConceptFrom() {return conceptFrom;}
	public void setConceptFrom(Concept conceptFrom) {this.conceptFrom = conceptFrom;}

	public Concept getConceptTo() {return conceptTo;}
	public void setConceptTo(Concept conceptTo) {this.conceptTo = conceptTo;}
	
	public Concept getRelationshipDefinition() {return relationshipDefinition;}
	public void setRelationshipDefinition(Concept relationshipDefinition) {this.relationshipDefinition = relationshipDefinition;}

}
