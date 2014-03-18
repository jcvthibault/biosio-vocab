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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Child-parent relationship between 2 semantic types
 * @author Julien Thibault, University of Utah
 *
 */
@Entity
@Table(name="SEMANTIC_TYPE_PARENT_REL")
public class SemanticTypeRelationship
{
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
	private Long id;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="tid_child", referencedColumnName="tid")
	private SemanticType childSemanticType;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="tid_parent", referencedColumnName="tid")
	private SemanticType parentSemanticType;
	
	public SemanticTypeRelationship(){
	}

	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}
	
	public SemanticType getChildSemanticType() {return childSemanticType;}
	public void setChildSemanticType(SemanticType childSemanticType) {this.childSemanticType = childSemanticType;}

	public SemanticType getParentSemanticType() {return parentSemanticType;}
	public void setParentSemanticType(SemanticType parentSemanticType) {this.parentSemanticType = parentSemanticType;}
	
}
