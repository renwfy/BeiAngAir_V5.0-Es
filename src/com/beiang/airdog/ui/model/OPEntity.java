package com.beiang.airdog.ui.model;

import com.beiang.airdog.constant.Constants.Command;

/***
 * 
 * 操作模型
 * 
 * @author LSD
 * 
 */
public class OPEntity {
	int devType;
	int subDevType;
	Command command;
	int function;
	BusinessEntity bEntity;

	public int getDevType() {
		return devType;
	}

	public void setDevType(int devType) {
		this.devType = devType;
	}

	public int getSubDevType() {
		return subDevType;
	}

	public void setSubDevType(int subDevType) {
		this.subDevType = subDevType;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	public int getFunction() {
		return function;
	}

	public void setFunction(int function) {
		this.function = function;
	}

	public BusinessEntity getbEntity() {
		return bEntity;
	}

	public void setbEntity(BusinessEntity bEntity) {
		this.bEntity = bEntity;
	}
}
