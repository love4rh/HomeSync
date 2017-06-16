package com.tool4us.homesync;


/**
 * String definition for languages
 * @author TurboK
 */
public enum LangPack
{
	R;
	
	public final int	APP_TITLE = 0;
	
	private String		_lang = System.getProperty("user.language");
	
	private LangPack()
	{
		//
		// System.getProperties("user.language")
	}
	
	public String string(int id)
	{
		return null;
	}
}

