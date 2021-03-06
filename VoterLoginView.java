import java.awt.Color;
import java.awt.GridLayout;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class VoterLoginView extends JPanel
{
	private Launcher currentDriver;
	
	private TextField firstName;
	private double usernameX, usernameY;
	private TextField lastName;
	private double passwordX, passwordY;
	private TextField birthday;
	private double birthdayX, birthdayY;
	private TextField address;
	private double addressX, addressY;
	private TextField city;
	private double cityX, cityY;
	private JComboBox<String> state;
	private double stateX, stateY;
	private TextField zip;
	private double zipX, zipY;
	private JButton login;
	private double loginX, loginY;
	private JButton back;
	private double backX, backY;
	private JButton register;
	private double registerX, registerY;
	
	private Color invalid = new Color(255, 225, 200);
	private Color defaultBackground = new Color(250, 250, 250);
	
	private boolean loaded;
	
	private String[] voterInfo;
	private static String[] voterWords = {"VUID", "First Name", "Last Name", "Address", "City", "State", "Zip", "Birthday", "Sex", "Race", "Expiration Date"};
	
	public static String[] getVoterWords() {
		return voterWords;
	}
	
	public VoterLoginView(Launcher l)
	{
		voterInfo = new String[11];
		currentDriver = l;
		
		this.setLayout(null);
		
		firstName = new TextField();
		firstName.setPlaceholder(voterWords[1]);
		firstName.setBackground(defaultBackground);
		firstName.setSize(100, 50);
		usernameX = 5/18.0;
		usernameY = 1/5.0;
		
		lastName = new TextField();
		lastName.setPlaceholder(voterWords[2]);
		lastName.setBackground(defaultBackground);
		lastName.setSize(100, 50);
		passwordX = 9/18.0;
		passwordY = 1/5.0;
		
		birthday = new TextField();
		birthday.setPlaceholder(voterWords[7] + " MMDDYYYY");
		birthday.setBackground(defaultBackground);
		birthday.setSize(150,50);
		birthdayX = 13/18.0;
		birthdayY = 1/5.0;
		
		address = new TextField();
		address.setPlaceholder(voterWords[3]);
		address.setBackground(defaultBackground);
		address.setSize(150, 50);
		addressX = 3/16.0;
		addressY = 1/2.0;
		
		city = new TextField();
		city.setPlaceholder(voterWords[4]);
		city.setBackground(defaultBackground);
		city.setSize(100,50);
		cityX = 7/16.0;
		cityY = 1/2.0;
		
		state = new JComboBox<String>(Candidate.getStatesList());
		state.insertItemAt(voterWords[5],0); //Check they don't leave "State" selected
		state.setSelectedIndex(0);
		state.setBackground(defaultBackground);
		state.setSize(100,20);
		stateX = 13/20.0;
		stateY = 1/2.0;
		
		zip = new TextField();
		zip.setPlaceholder(voterWords[6] + " (5 digits)");
		zip.setBackground(defaultBackground);
		zip.setSize(100,50);
		zipX = 17/20.0;
		zipY = 1/2.0;
		
		login = new JButton();
		login.setText("Login");
		login.setSize(200,50);
		loginX = 2.5/5.0;
		loginY = 8/10.0;
		login.addActionListener(e -> {
			if(validateInput())
			{	
				voterInfo = new String[11];
				//voterInfo[0] = VUID
				voterInfo[1] = firstName.getText().trim().toUpperCase();
				voterInfo[2] = lastName.getText().trim().toUpperCase();
				voterInfo[3] = address.getText().trim().toUpperCase();
				voterInfo[4] = city.getText().trim().toUpperCase();
				voterInfo[5] = (String)state.getSelectedItem();
				voterInfo[6] = zip.getText().trim();
				voterInfo[7] = birthday.getText().trim();
				//voterInfo[8] = Sex
				//voterInfo[9] = Ethnicity
				//voterInfo[10] = Registration Expiration Date

				String searchKey = Voter.getSearchKeyNAD(voterInfo);
				long locInFile;

				if(!Voter.checkKeyNAD(searchKey))
				{
					//Go to Voter Registration page
					currentDriver.setEnabled(false);
					JFrame popup = new JFrame();
					popup.setLayout(new GridLayout(2,2));
					popup.setLocationRelativeTo(null);
					popup.setSize(600,400);	
					popup.setResizable(false);
					popup.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					JLabel p1 = new JLabel("You were not in the system,");
					p1.setHorizontalAlignment(JLabel.RIGHT);
					popup.add(p1);
					popup.add(new JLabel(" please register or try again."));
					JPanel pan1 = new JPanel();
					JButton tryAgain = new JButton("Try Again");
					tryAgain.addActionListener(e2 -> {
						currentDriver.setEnabled(true);
						popup.dispose();
					});
					pan1.add(tryAgain);
					JPanel pan2 = new JPanel();
					JButton pleaseRegister = new JButton("Register");
					pleaseRegister.addActionListener(e2 -> {
						// Remove capitalization for registration screen
						voterInfo[1] = firstName.getText().trim();
						voterInfo[2] = lastName.getText().trim();
						voterInfo[3] = address.getText().trim();
						voterInfo[4] = city.getText().trim();
						voterInfo[5] = (String)state.getSelectedItem();
						voterInfo[6] = zip.getText();
						voterInfo[7] = birthday.getText();
						currentDriver.setEnabled(true);
						currentDriver.switchScene(new RegisterVoterInfoView(currentDriver,voterInfo));
						popup.dispose();
					});
					pan2.add(pleaseRegister);
					popup.add(pan1);
					popup.add(pan2);
					popup.setVisible(true);
				}
				else
				{
					locInFile = Voter.getKeyValNAD(searchKey);
					voterInfo = Voter.lookup(locInFile);
					
					if(Voter.isExpired(voterInfo[10])) {
						voterInfo = Voter.editLine(new String[] {Voter.genExpDate()}, 10, locInFile);
						JFrame popup = new JFrame();
						popup.setLayout(new GridLayout(2,2));
						popup.setLocationRelativeTo(null);
						popup.setSize(600,400);	
						popup.setResizable(false);
						popup.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
						JLabel p1 = new JLabel("Your VUID was expired,");
						p1.setHorizontalAlignment(JLabel.RIGHT);
						popup.add(p1);
						popup.add(new JLabel(" press ok to renew."));
						JPanel pan1 = new JPanel();
						JPanel pan2 = new JPanel();
						JButton ok = new JButton("Ok");
						ok.addActionListener(e2 -> {
							currentDriver.setEnabled(true);
							popup.dispose();
						});
						pan1.add(ok);
						popup.add(pan2);
						popup.add(pan1);
						popup.setVisible(true);
						popup.setAlwaysOnTop(true);
					}
					currentDriver.switchScene(new VoterHomeView(currentDriver,voterInfo, locInFile));
				}
			}
		});
		
		back = new JButton();
		back.setText("Back");
		back.setSize(100,50);
		backX = 1/5.0;
		backY = 8/10.0;
		back.addActionListener(e -> {
			try {
				currentDriver.switchScene(new HomeView(l));
			} catch (InterruptedException interruptedException) {
				interruptedException.printStackTrace();
			}
		});

		register = new JButton();
		register.setText("Register");
		register.setSize(100,50);
		registerX = 4/5.0;
		registerY = 8/10.0;
		register.addActionListener(e -> {
			
			voterInfo = new String[11];
			voterInfo[1] = firstName.getIsDefault()?null: firstName.getText().trim().toUpperCase();
			voterInfo[2] = lastName.getIsDefault()?null: lastName.getText().trim().toUpperCase();
			voterInfo[3] = address.getIsDefault()?null:address.getText().trim().toUpperCase();
			voterInfo[4] = city.getIsDefault()?null:city.getText().trim().toUpperCase();
			voterInfo[5] = (String)state.getSelectedItem();
			voterInfo[6] = zip.getIsDefault()?null:zip.getText().trim();
			voterInfo[7] = birthday.getIsDefault()?null:birthday.getText().trim();
			System.out.println("voterInfo: " + Arrays.toString(voterInfo));
			currentDriver.switchScene(new RegisterVoterInfoView(currentDriver, voterInfo));
		});
		
		this.add(firstName);
		this.add(lastName);
		this.add(birthday);
		this.add(address);
		this.add(city);
		this.add(state);
		this.add(zip);
		this.add(login);
		this.add(back);
		this.add(register);
		
		loaded = true;
	}
	
	public void repaint()
	{
		if(!loaded)
		{
			return;
		}
		
		int x = this.getWidth();
		int y = this.getHeight();
		firstName.setBounds((int)(x*usernameX- firstName.getWidth()/2), (int)(y*usernameY- firstName.getHeight()/2), firstName.getWidth(), firstName.getHeight());
		lastName.setBounds((int)(x*passwordX- lastName.getWidth()/2), (int)(y*passwordY- lastName.getHeight()/2), lastName.getWidth(), lastName.getHeight());
		birthday.setBounds((int)(x*birthdayX-birthday.getWidth()/2), (int)(y*birthdayY-birthday.getHeight()/2), birthday.getWidth(), birthday.getHeight());
		address.setBounds((int)(x*addressX-address.getWidth()/2), (int)(y*addressY-address.getHeight()/2), address.getWidth(), address.getHeight());
		city.setBounds((int)(x*cityX-city.getWidth()/2), (int)(y*cityY-city.getHeight()/2), city.getWidth(), city.getHeight());
		state.setBounds((int)(x*stateX-state.getWidth()/2), (int)(y*stateY-state.getHeight()/2), state.getWidth(), state.getHeight());
		zip.setBounds((int)(x*zipX-zip.getWidth()/2), (int)(y*zipY-zip.getHeight()/2), zip.getWidth(), zip.getHeight());
		login.setBounds((int)(x*loginX-login.getWidth()/2), (int)(y*loginY-login.getHeight()/2), login.getWidth(), login.getHeight());
		back.setBounds((int)(x* backX - back.getWidth()/2), (int)(y* backY - back.getHeight()/2), back.getWidth(), back.getHeight());
		register.setBounds((int)(x* registerX - register.getWidth()/2), (int)(y* registerY - register.getHeight()/2), register.getWidth(), register.getHeight());
	}

	private boolean validateInput()
	{
		boolean isValid = true;

		if(!firstName.getText().matches("^[a-zA-Z_' ]*$") || firstName.getText().equals("First Name")
				||firstName.getText().length() > 20)
		{
			firstName.setBackground(invalid);
			isValid = false;
		}
		else
		{

			firstName.setBackground(defaultBackground);
		}

		if(!lastName.getText().matches("^[a-zA-Z_' ]*$") || lastName.getText().equals("Last Name")
				|| lastName.getText().length() > 20 )
		{
			lastName.setBackground(invalid);
			isValid = false;
		}
		else
		{
			lastName.setBackground(defaultBackground);
		}

		if(!birthday.getText().matches("\\d{8}") || (Voter.getAge(birthday.getText()) < 18 ))
		{
			birthday.setBackground(invalid);
			isValid = false;
		}
		else
		{
			birthday.setBackground(defaultBackground);
		}

		if(!address.getText().matches("^[a-zA-Z0-9_' ]*$") || address.getText().equals("Address")
				|| address.getText().length() > 40 )
		{
			address.setBackground(invalid);
			isValid = false;
		}
		else
		{
			address.setBackground(defaultBackground);
		}

		if(!city.getText().matches("^[a-zA-Z_' ]*$") || city.getText().equals("City")
				|| lastName.getText().length() > 20 )
		{
			city.setBackground(invalid);
			isValid = false;
		}
		else
		{
			city.setBackground(defaultBackground);
		}

		if(state.getSelectedItem().equals("State"))
		{
			state.setBackground(invalid);
			isValid = false;
		}
		else
		{
			state.setBackground(defaultBackground);
		}

		if(!zip.getText().matches("\\d{5}"))
		{
			zip.setBackground(invalid);
			isValid = false;
		}
		else
		{
			zip.setBackground(defaultBackground);
		}

		return isValid;
	}
}