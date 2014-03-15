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
 * Citation
 * @author Julien Thibault, University of Utah
 *
 */
@Entity
@Table(name="CITATION")
public class Citation {

	private Long id;
	private String source;
	private Integer year;
	private String doi;
	private String text;

	public Citation(){
	}

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	@Column(name = "source")
	public String getSource() {return source;}
	public void setSource(String source) {this.source = source;}

	@Column(name = "year")
	public Integer getYear() {return year;}
	public void setYear(Integer year) {this.year = year;}

	@Column(name = "doi")
	public String getDoi() {return doi;}
	public void setDoi(String doi) {this.doi = doi;}

	@Column(name = "str")
	public String getText() {return text;}
	public void setText(String text) {this.text = text;}

}
