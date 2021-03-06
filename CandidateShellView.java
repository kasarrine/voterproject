import java.awt.Color;

import javax.swing.JComboBox;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CandidateShellView extends JPanel
{
	private AddCandidateView parent;
	
	private TextField name;
    private double nameY;
    private JComboBox<String> state;
    private double stateY;
    private JComboBox<String> position;
    private double positionY;
    private JComboBox<String> party;
    private double partyY;
    
    private Color invalid = new Color(255, 225, 200);
    private Color defaultBackground = new Color(250, 250, 250);

    private String[] parties =
        {"Democrat", "Republican", "Third Party"};

    private String[] positions =
        {"President/Vice", "Senator", "Congress", "Governor", "Secretary", "Treasurer", "Justice"};
    
    private boolean isVice;
    private boolean loaded;
	
	public CandidateShellView(AddCandidateView p, boolean iv)
	{
		parent = p;
		isVice = iv;
		
		this.setLayout(null);
		
		name = new TextField();
		if(!isVice) {
			name.setPlaceholder("Candidate Name");
		} else {
			name.setPlaceholder("Vice Name");
		}  
        name.setBackground(defaultBackground);
        name.setSize(250, 50);
        nameY = 1/10.0;
        
        state = new JComboBox<String>(Candidate.getStatesList());
        state.insertItemAt("State", 0);
        state.setSelectedIndex(0);
        state.setSize(150, 20);
        stateY = 3/10.0;
        
        position = new JComboBox<String>(positions);
        position.insertItemAt("Position", 0);
        position.setSelectedIndex(0);
        position.setSize(150, 20);
        positionY = 5/10.0;
        position.addActionListener(e -> {
        	if(position.getSelectedItem().equals(positions[0]))
        	{
        		state.setEnabled(false);
        		parent.setVice(true);
        	}
        	else
        	{
        		parent.setVice(false);
        		state.setEnabled(true);
        	}
        	parent.setSize(parent.getWidth(), parent.getHeight()+1);
        	parent.setSize(parent.getWidth(), parent.getHeight()-1);
        });
        
        party = new JComboBox<String>(parties);
        party.insertItemAt("Party", 0);
        party.setSelectedIndex(0);
        party.setSize(150, 20);
        partyY = 7/10.0;
        
        if(!isVice)
        {
        	this.add(state);
        	this.add(party);
        	this.add(position);
        }
        this.add(name);
		
		loaded = true;
	}
	
	public void repaint()
	{
		if(!loaded)
		{
			return;
		}
		
		int y = this.getHeight();
		
		name.setBounds(10, (int)(y*nameY), name.getWidth(), name.getHeight());
		state.setBounds(10, (int)(y*stateY), state.getWidth(), state.getHeight());
		position.setBounds(10, (int)(y*positionY), position.getWidth(), position.getHeight());
		party.setBounds(10, (int)(y*partyY), party.getWidth(), party.getHeight());
	}
	
	public boolean validateInput()
	{
		boolean isValid = true;
		
		if(name.getIsDefault())
		{
			isValid = false;
			name.setBackground(invalid);
		}
		else
		{
			name.setBackground(defaultBackground);
		}
		
		if(!isVice)
		{
			if(state.getSelectedItem().equals("State") && !position.getSelectedItem().equals(positions[0]))
			{
				isValid = false;
				state.setBackground(invalid);
			}
			else
			{
				state.setBackground(defaultBackground);
			}
			
			if(position.getSelectedItem().equals("Position"))
			{
				isValid = false;
				position.setBackground(invalid);
			}
			else
			{
				position.setBackground(defaultBackground);
			}
			
			if(party.getSelectedItem().equals("Party"))
			{
				isValid = false;
				party.setBackground(invalid);
			}
			else
			{
				party.setBackground(defaultBackground);
			}
		}
		
		return isValid;
	}
	
	public String getPosition()
	{
		return (String)position.getSelectedItem();
	}
	
	public String getState()
	{
		return (String)state.getSelectedItem();
	}
	
	public String getParty()
	{
		return (String)party.getSelectedItem();
	}
	
	public String getName()
	{
		return name.getText();
	}
}
