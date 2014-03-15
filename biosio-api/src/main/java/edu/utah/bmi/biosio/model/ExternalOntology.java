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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Ontology definition (for imports and mappings)
 * @author Julien Thibault - University of Utah, BMI
 *
 */
@Entity
@Table(name="EXTERNAL_ONTOLOGY")
public class ExternalOntology 
{
	private String id;
	private String name;
	private String prefix;
	private String documentURL;
	private String description;
	
	@Id @Column(name = "id")
	public String getId() {return id;}
	public void setId(String id) {this.id = id;}
	
	@Column(name = "name")
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	
	@Column(name = "prefix")
	public String getPrefix() {return prefix;}
	public void setPrefix(String prefix) {this.prefix = prefix;}

	@Column(name = "document_url")
	public String getDocumentURL() {return this.documentURL;}
	public void setDocumentURL(String documentURL) {this.documentURL = documentURL;}
	
	@Column(name = "description")
	public String getDescription() {return this.description;}
	public void setDescription(String description) {this.description = description;}
}
