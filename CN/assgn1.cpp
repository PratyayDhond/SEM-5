#include <bits/stdc++.h>
using namespace std;
#define ull unsigned long long
class Validator
{
private:
public:
	unordered_map<char, vector<string>> mp;
	Validator()
	{
		mp['a'] = {"10.", "8", "12"};
		mp['b'] = {"172", "16", "24"};
		mp['c'] = {"192", "24", "30"};
	}

	bool validate_class(char c)
	{
		return !(mp.find(c) == mp.end());
	}
	bool validate_IPaddr(char c, string IPaddr)
	{
		string wanted = mp[c][0];
		string given = IPaddr.substr(0, 3);
		if (wanted == given)
		{
			stringstream ss(IPaddr);
			string token;
			while (getline(ss, token, '.'))
			{
				if (stoi(token) < 0 && stoi(token) > 255)
				{
					return false;
				}
			}
			return true;
		}
		return false;
	}
	bool validate_mask(char c, int sbnet_mask)
	{
		return sbnet_mask >= stoi(mp[c][1]) && sbnet_mask <= stoi(mp[c][2]);
	}
};

string bitstodotdec(bitset<32> bitmask)
{
	stringstream subnetMaskStream;
	for (int i = 0; i < 4; ++i)
	{
		int octet = 0;
		for (int j = 0; j < 8; ++j)
		{
			octet <<= 1;
			octet |= bitmask[i * 8 + j];
		}
		subnetMaskStream << octet;
		if (i < 3)
			subnetMaskStream << ".";
	}
	string subnetMask = subnetMaskStream.str();
	return subnetMask;
}
bitset<32> addWithCarry(const bitset<32> &inputBitset, int numberToAdd, bool carry)
{
	bitset<32> resultBitset;
	int carryBit = carry ? 1 : 0;

	for (int i = 0; i < 32; ++i)
	{
		int sum = inputBitset[i] + (numberToAdd & 1) + carryBit;
		resultBitset[i] = sum & 1;
		carryBit = sum >> 1;
		numberToAdd >>= 1;
	}

	return resultBitset;
}
bitset<32> ipToBitset(const string &ipAddress)
{
	stringstream ss(ipAddress);
	vector<int> octets;
	int octet;

	while (ss >> octet)
	{
		octets.push_back(octet);
		if (ss.peek() == '.')
		{
			ss.ignore();
		}
	}

	if (octets.size() != 4)
	{
		throw runtime_error("Invalid IP address format");
	}

	bitset<32> ipBitset;
	for (size_t i = 0; i < octets.size(); ++i)
	{
		for (int j = 0; j < 8; ++j)
		{
			ipBitset[i * 8 + j] = (octets[i] >> (7 - j)) & 1;
		}
	}

	return ipBitset;
}
bitset<32> and_bitsets(bitset<32> a, bitset<32> b)
{
	for (int i = 0; i < 32; i++)
	{
		a[i] = a[i] & b[i];
	}
	return a;
}
bitset<32> neg_bitset(bitset<32> a)
{
	for (int i = 0; i < 32; i++)
	{
		a[i] = !(a[i]);
	}
	return a;
}
bitset<32> or_bitsets(bitset<32> a, bitset<32> b)
{
	for (int i = 0; i < 32; i++)
	{
		a[i] = a[i] | b[i];
	}
	return a;
}
bitset<32> rev_bitset(bitset<32> a)
{
	bitset<32> res;
	for (int i = 0; i < 32; i++)
	{
		res[i] = a[31 - i];
	}
	return res;
}
string addValueToIPAddress(const string &ipAddressStr, int value) {
    stringstream ss(ipAddressStr);
    vector<int> octets;
    int octet;

    while (ss >> octet) {
        octets.push_back(octet);
        if (ss.peek() == '.') {
            ss.ignore();
        }
    }

    if (octets.size() != 4) {
        throw runtime_error("Invalid IP address format");
    }

    // Add the value to the last octet
    octets[3] += value;

    // Handle carry
    for (int i = 3; i >= 0; --i) {
        if (octets[i] > 255) {
            if (i > 0) {
                octets[i - 1] += octets[i] / 256;
                octets[i] %= 256;
            }
            else {
                // If we have carry in the first octet, we may need to extend the IP address
                octets[i] %= 256; // Keep only the least significant 8 bits
                octets.insert(octets.begin(), octets[i] / 256); // Add the carry as a new octet
            }
        }
    }

    // Format the result
    stringstream resultStream;
    for (size_t i = 0; i < octets.size(); ++i) {
        resultStream << octets[i];
        if (i < 3) {
            resultStream << ".";
        }
    }

    return resultStream.str();
}
static bool cmp(string a, string b){
	stringstream ss1(a);
	stringstream ss2(b);
	string octet;
	ull sm1=0,sm2=0;
	while (getline(ss1,octet,'.')) {
        sm1+=stoi(octet);
    }
	while (getline(ss2,octet,'.')) {
        sm2+=stoi(octet);
    }
	return sm1<sm2;
}
int main()
{
	/*
	 1. IP: class, IP addr, subnet mask
	 2. OP: subnets, no of hosts per subnet, valid range, n/w id, broadcast id
	 */
	Validator vld;
	char c;
	string IPaddr;
	int sbnet_msk;
	cout << "Enter IP address class, IP address and Subnet Mask(/set bits):\n";
	cin >> c >> IPaddr >> sbnet_msk;
	c = tolower(c);
	if (vld.validate_class(c) && vld.validate_IPaddr(c, IPaddr) && vld.validate_mask(c, sbnet_msk))
	{
		// Number of subnets= 2^sbnet_msk - normalsbnet
		int normalsbnet = stoi(vld.mp[c][1]);
		ull sbnets = (ull)pow(2, sbnet_msk - normalsbnet);
		cout << "Number of subnets: " << sbnets << endl;

		// Number of host per subnet= total ip addr/no of subnets -2
		ull totalIPaddresses = (ull)pow(2, 32 - normalsbnet);
		ull hostpersubnet = ((totalIPaddresses) / sbnets) - 2;
		cout << "Number of Hosts per subnet: " << hostpersubnet << endl;
		cout << "========================================================================" << endl;
		cout << "Subnet Mask\tNetwork Id\tBroadcaste ID\tValid Range" << endl;
		// Create subnet mask
		bitset<32> maskBits;
		ull bitstoset = normalsbnet;
		for (int i = 0; i < sbnet_msk; i++)
		{
			maskBits[i] = 1;
		}
		ull numberToAdd = (1 << bitstoset);
		bool carry = false;
		// Generating all Subnets by addition
		bitset<32> convted_ip_addr=ipToBitset(IPaddr);
		vector<string> sbnet_masks;
		sbnet_masks.push_back(bitstodotdec(maskBits));
		bitset<32> networkID=and_bitsets(convted_ip_addr,maskBits);
		for (int i = 1; i < sbnets; ++i)
		{
			sbnet_masks.push_back(bitstodotdec(maskBits));
		}
		sort(sbnet_masks.begin(),sbnet_masks.end(),cmp);


		string next_nw_id=bitstodotdec(networkID);
		string broadcaste_id=addValueToIPAddress(next_nw_id,hostpersubnet+1);
		string lowerAddr=addValueToIPAddress(next_nw_id,1);
		string upperAddr=addValueToIPAddress(next_nw_id,hostpersubnet);
		for(string s: sbnet_masks){
			cout <<s<<"\t"<<next_nw_id<<"\t"<<broadcaste_id<<"\t"<<lowerAddr<<"-"<<upperAddr<<endl;
			next_nw_id=addValueToIPAddress(next_nw_id,hostpersubnet+2);
			broadcaste_id=addValueToIPAddress(next_nw_id,hostpersubnet+1);
			lowerAddr=addValueToIPAddress(next_nw_id,1);
			upperAddr=addValueToIPAddress(next_nw_id,hostpersubnet);
		}
		
	}
	else{
		cout<<"\nINVALID I/P DETECTTED!"<<endl;
	}
	return 0;
}
