package gui;

public class ComboItem {
	private String key;
    private String value;

    public ComboItem(String value,String key)
    {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString()
    {
        return key;
    }

    public String getKey()
    {
        return key;
    }

    public String getValue()
    {
        return value;
    }
}
