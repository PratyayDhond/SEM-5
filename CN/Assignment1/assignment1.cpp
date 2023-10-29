#include<bits/stdc++.h>
using namespace std;

enum NetworkClass{
    nullClass,
    ClassA,
    ClassB,
    ClassC
};

enum IpType{
    unassignedType,
    IPV4,
    IPV6
};

// Error Class
class SubnetBitLimitExceededError{};
class InsufficientHostIdBitsError{};
class InvalidNetworkClassException{};
class InvalidIpErrorException{};
class InsufficientHostAddressesException{};

class SubnetMask{
    private:
        vector<int> octates;
        NetworkClass currentNetworkClass;
        int networkBits;
        int subnetBits;
        int hostBits;
    public:
        // SubnetMask(){
        //     octates.resize(4);
        //     currentNetworkClass = nullClass;
        //     subnetBits = 0;
        // }

        SubnetMask(NetworkClass ClassName = nullClass){
            octates.resize(4);
            subnetBits = 0;
            hostBits = 0;
            networkBits = 0;
            try{
                if(ClassName == nullClass)
                    throw InvalidNetworkClassException();
                currentNetworkClass = ClassName;
                setNaturalMask();
            }catch(InvalidNetworkClassException e){
                cout << "Invalid Network Class Selected. Current Network class value: " << getNetworkClass() <<"\n";
                cout << "You will be redirected to setting Network Class Again.\n";
                currentNetworkClass = getNetworkClass();
            }
            return;
        }

        vector<int> getMask(){
            return octates;
        }

        NetworkClass getNetworkClass(){
            return currentNetworkClass;
        }

        int getSubnetBits(){
            return this->subnetBits;
        }

        int getHostbits(){
            return this->hostBits;
        }

        int getNetworkIdBits(){
            return this->networkBits;
        }
        void setNaturalMask(int ClassName = -1){
            if(ClassName == -1)
                ClassName = currentNetworkClass;
            else
                currentNetworkClass = (NetworkClass) ClassName;

            if(currentNetworkClass > 3)
                currentNetworkClass = nullClass;
            if(ClassName == 1)
                octates = {255,0,0,0};
            else if(ClassName == 2)
                octates = {255,255,0,0};
            else
                octates = {255,255,255,0};
            return;
        }

        void setMask(int maskBits){
                int remainingBits = 32 - currentNetworkClass * 8;
                int hostIdBits = remainingBits - maskBits;
            try{
                if(hostIdBits <= 0)
                    throw SubnetBitLimitExceededError();
                if(hostIdBits == 1)
                    throw InsufficientHostIdBitsError();

                int customMaskSize = 0;
                if(getNetworkClass() == ClassA)
                    customMaskSize = 3;
                else if(getNetworkClass() == ClassB)
                    customMaskSize = 2;
                else if(getNetworkClass() == ClassC)
                    customMaskSize = 1;
                else
                    throw InvalidNetworkClassException();

                string customMask[customMaskSize] = {""};   // 9th bit for \0
                int currentOctate = 0;
                int currentBit = 0;
                int i = 0;
                for(; i < maskBits; i++){
                    currentOctate = i / 8;
                    customMask[currentOctate] += '1';
                    // customMask[currentOctate][currentBit] += '1';
                }
                for(; i < customMaskSize * 8; i++){
                    currentOctate = i / 8;
                    currentBit = i % 8;
                    customMask[currentOctate] += '0';
                }
                // for(auto a : customMask)             // prints all the bits of the current custom mask
                //     cout << "--" << a << "--";
                // cout << endl;
                for(int i = (int) getNetworkClass(), currentMask = 0; i <= (int) ClassC; i++){
                    octates[i] = stoi(customMask[currentMask++],0,2);
                }

            }catch(SubnetBitLimitExceededError e){
                cout << "Error! Insufficient bits for subnet masking.\n You can use 0-" <<  remainingBits-2 << " bits as a subnet.\n";
                int maskBits = 0;
                cout << "Enter maskBits: ";
                cin >> maskBits;
                return setMask(maskBits);
            }catch(InsufficientHostIdBitsError e){
                cout << "Error! Insufficient bits left for host ID. You need at least 2 bits kept for HOST ID.\nYour input leaves " << hostIdBits << " remaining. \nYou can use 0-" <<  remainingBits-2 << " bits as a subnet.\n";
                int maskBits = 0;
                cout << "Enter maskBits: ";
                cin >> maskBits;
                return setMask(maskBits);
            }catch(InvalidNetworkClassException e){
                cout << "Invalid Network Class Selected. Current Network class value: " << getNetworkClass() <<"\n";
                cout << "You will be redirected to setting Network Class Again.\n";
                currentNetworkClass = getNetworkClass();
                setMask(maskBits);
            }catch(...){
                cout << "Some Exception has occured. Exitting Program";
                exit(0);
            }
            this->subnetBits = maskBits;
            this->hostBits = hostIdBits; // inclusive of network id and broadcast id.
            this->networkBits = this->currentNetworkClass * 8;
        }

        int getNumberOfPossibleSubnets(){
            return pow(2,this->subnetBits);
        }

        int getNumberOfPossibleHosts(){
            return pow(2,this->hostBits);
        }


};

class IPAddress{
    private:
        IpType IpAddressType;
        vector<int> IpAddressOctates;
    public:
        IPAddress(IpType IP_ADDRESS_TYPE = IPV4){
            this->IpAddressType = IP_ADDRESS_TYPE;
            IpAddressOctates.resize(4);
        }

        vector<int>     getIpAddress(){
            return IpAddressOctates;
        }
        
        IpType getIpType(){
            return IpAddressType;
        }

        void setIpAddress(string IpString){
            // cout << IpString << endl;

            int j = 0;
            int i = 0;
            int OctateNumber = 0;
            for(;i < IpString.size(); i++){
                
                if(IpString[i] == '.'){
                    string IpOctate = IpString.substr(j,i);
                    IpAddressOctates[OctateNumber++] = stoi(IpOctate);
                    j = i+1;
                }
            }
            string IpOctate = IpString.substr(j,i);
            IpAddressOctates[OctateNumber++] = stoi(IpOctate);
            return;
        }
};

NetworkClass getNetworkClass(){
    try{
        char ans = ' ';
        while(ans != 'A' && ans != 'B' && ans != 'C'){
            cout << "Enter Network Class: (Options A, B, C): ";
            cin >> ans;
            ans = toupper(ans);
        }
        if(ans == 'A')
            return ClassA;
        else if(ans == 'B')
            return ClassB;
        else
            return ClassC;
    }catch(...){
        system("cls");
        cout << "Exception has occured in getNetworkClass function recalling the same function recursively.";
        return getNetworkClass();
    }
}

void displaySubnetMask(SubnetMask subnetMask){
    int count = 0;
    for(auto a : subnetMask.getMask()){
        cout << a;
        if(count++ < 3)
            cout <<".";
    }
    cout << endl;
    return;
}

int getSubnetBits(SubnetMask subnetMask){
    int remainingBits = 32 - subnetMask.getNetworkClass() * 8;
    int maskBits = 0;
    int hostIdBits = 0;
    try{
        cout << "Enter maskBits: ";
        cin >> maskBits;
        hostIdBits = remainingBits - maskBits;
        if(hostIdBits <= 0)
            throw SubnetBitLimitExceededError();
        if(hostIdBits == 1)
            throw InsufficientHostIdBitsError();        
    }catch(SubnetBitLimitExceededError e){
                cout << "Error! Insufficient bits for subnet masking.\n You can use 0-" <<  remainingBits-2 << " bits as a subnet.\n";
                maskBits = 0;
                cout << "Enter maskBits: ";
                cin >> maskBits;
    }catch(InsufficientHostIdBitsError e){
                cout << "Error! Insufficient bits left for host ID. You need at least 2 bits kept for HOST ID.\nYour input leaves " << hostIdBits << " remaining. \nYou can use 0-" <<  remainingBits-2 << " bits as a subnet.\n";
                maskBits = 0;
                cout << "Enter maskBits: ";
                cin >> maskBits;
    }catch(...){
        cout << "Unhandled Exception has occured in getSubnetBits -> catch(...) exception";
        exit(0);
    }
    return maskBits;
}

string getIpString(){
    // provides validation of IP String and returns valid IP String
    string ipAddress;
    int dotCount = 0;
    try{       
        cout << "Enter IP Address : ";
        cin >> ipAddress;
        int i = 0;
        int j = 0;
        for(; i < ipAddress.size(); i++){
            if(ipAddress[i] == '.'){
                dotCount++;
                int octateValue = stoi(ipAddress.substr(j,i));
                if(octateValue > 255 || octateValue < 0)
                    throw InvalidIpErrorException();
                j = i+1;
            }
        }
        int octateValue = stoi(ipAddress.substr(j,i));

        if(octateValue > 255 || octateValue < 0 || dotCount > 3 || dotCount < 3) throw InvalidIpErrorException();
    }catch(InvalidIpErrorException e){
        cout <<"You have entered an Invalid IP Address\nAn IPV4 Address can have only 4 parts ranging from 0 to 255 inclusive only.\n";
        return getIpString();
    }
        return ipAddress;

}

vector<int> getBroadCastID(SubnetMask subnetMask, IPAddress ipAddress){
    vector<string> broadcastIdOctates(4,"11111111");
    vector<string> networkId(4,"11111111");
    vector<int> octates;
    int octateCount = 0;

    for(int i = 0; i < 4; i++){
        broadcastIdOctates[i] = bitset<8>(subnetMask.getMask()[i] & ipAddress.getIpAddress()[i]).to_string();
    }

    int i = subnetMask.getSubnetBits(); 
    for(int octate = subnetMask.getNetworkClass(); octate < 4; octate++){
        for(; i < 8; i++)
            broadcastIdOctates[octate][i] = '1';
        if(i >= 8)
            i -= 8;
    }

    int count = 0;
    for(auto a : broadcastIdOctates){
        octates.push_back(stoi(a,0,2));
        // cout << stoi(a,0,2);
        // if(count++ < 3)
        //     cout << ".";
    }
    return octates;
}

vector<int> getNetworkID( SubnetMask subnetMask, IPAddress ipAddress){
    int count = 0;
    vector<int> octate;
    for(int i = 0; i < 4; i++){
        int ipVal = ipAddress.getIpAddress()[i];
        int subnetval = subnetMask.getMask()[i];
            octate.push_back(ipVal & subnetval);
    }
    return octate;
}

void displayAddress(vector<int> IpOctate, char endChar = '\n'){
    int count = 0;
    for(auto a : IpOctate){
        cout << a;
        if(count++ < 3)
            cout << ".";
    }
    cout << endChar;
    return;
}

void displayUsableHostRange(SubnetMask subnetMask, IPAddress ipAddress){
    try{
        vector<int> startOctate = getNetworkID(subnetMask,ipAddress);
        vector<int> endOctate = getBroadCastID(subnetMask,ipAddress);
        startOctate[3]++;
        endOctate[3]--;
        if(startOctate == endOctate)
            throw InsufficientHostAddressesException();
        displayAddress(startOctate, ' ');
        cout << " - ";
        displayAddress(endOctate,' ');
        cout << endl;
    }catch(InsufficientHostAddressesException i){
        cout<<"You do not have sufficient Addresses left for Host IDs.";
    }catch(...){
        cout<<"An Unhandled Exception has occurred at displayUsableHostRange() function";
    }
    return;
   
}

void displayDetails(SubnetMask subnetMask, IPAddress ipAddress){
    int count = 0;  // for printing the dots every time
    cout << "Updated Subnet Mask: ";
    displaySubnetMask(subnetMask);
    cout << "Subnet Bits : " << subnetMask.getSubnetBits() << "\n";

    cout << "Number of Possible Subnets : " << subnetMask.getNumberOfPossibleSubnets() << endl;
    cout << "Number of Hosts per Subnet : " << subnetMask.getNumberOfPossibleHosts() << endl;
    
    cout << "Network ID: ";
    displayAddress(getNetworkID(subnetMask,ipAddress));
    

    cout <<"Usable Host range: ";
    displayUsableHostRange(subnetMask, ipAddress);

    cout << "Broadcast ID: ";
    displayAddress(getBroadCastID(subnetMask,ipAddress));

}

int main(){
    // 
    /*
    Assignment 1
    Write a program as follows
        - Select the class (A, B, C)
        - Then print the subnet Mask of that particular class selected
        - Enter the IP address
        - print
        1. Number of subnets will be created.
        2. Number of hosts per subnet
        3. Network id and broad cast id of each network
        4. Range of subnet mask of that particular class selected*/

    // Input Class Name
    NetworkClass networkClass = getNetworkClass();
    SubnetMask subnetMask(networkClass);
    cout << "Natural Subnet Mask has been set. Subnet Mask: ";
    displaySubnetMask(subnetMask);
    int subnetBits = getSubnetBits(subnetMask);
    subnetMask.setMask(subnetBits);

    
    IPAddress ipAddress;
    ipAddress.setIpAddress(getIpString());

    displayDetails(subnetMask, ipAddress);
    // Input IP Address
    // Input CIDR 
    
}