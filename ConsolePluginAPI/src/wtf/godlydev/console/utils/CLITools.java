package wtf.godlydev.console.utils;

public abstract class CLITools {
	
    public abstract boolean setTitle(final String title);
    
    public abstract boolean clear();
    
    public abstract void blink();
	
	public abstract void bold();
	
	public abstract void italic();
	
	public abstract void mark();
	
	public abstract void reset();
	
	public abstract void reverse();
	
	public abstract void strikethrough();
	
	public abstract void underline();
	
	public abstract void upperline();
	
	public abstract void setColor(final String color);
	
	public abstract void setColorRGB(int red, int green, int blue);
	
	public abstract void setColorHEX(final String hex);
	
	public abstract void setBackground(final String color);
	
	public abstract void setBackgroundRGB(int red, int green, int blue);
	
	public abstract void setBackgroundHEX(final String hex);
    
}