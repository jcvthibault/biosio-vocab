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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Concept term (synonym)
 * @author Julien Thibault, University of Utah
 *
 */
@Entity
@Table(name="CONCEPT_TERM")
public class Synonym {

	private Long id;
	private String cui;
	private String term;
	private Boolean isAbbreviation;
	private String language;
	private Boolean isPreferredTerm;

	public Synonym(){
	}
	
	public Synonym(String cui, String language, boolean isAbbreviation, boolean isPreferredTerm){
		this.cui = cui;
		this.isAbbreviation = isAbbreviation;
		this.isPreferredTerm = isPreferredTerm;
		this.language = language;
	}

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}
	
	@Column(name = "cui")
	public String getCui() {return cui;}
	public void setCui(String cui) {this.cui = cui;}

	@Column(name = "str")
	public String getTerm() {return term;}
	public void setTerm(String term) {this.term = term;}
	
	@Column(name = "is_abbr")
	public Boolean getIsAbbreviation() {return isAbbreviation;}
	public void setIsAbbreviation(Boolean isAbbreviation) {this.isAbbreviation = isAbbreviation;}

	@Column(name = "lang")
	public String getLanguage() {return language;}
	public void setLanguage(String language) {this.language = language;}

	@Column(name = "is_pref")
	public Boolean getIsPreferredTerm() {return isPreferredTerm;}
	public void setIsPreferredTerm(Boolean isPreferredTerm) {this.isPreferredTerm = isPreferredTerm;}

	
}
