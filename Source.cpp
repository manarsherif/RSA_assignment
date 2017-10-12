#include <iostream>
#include <string>
#include <vector>
#include <math.h>
#include <map>
#include <vector>
#include <algorithm>
#include <ctime>

using namespace std;
vector<unsigned long long> Multiply(vector<unsigned long long> & p_digits, vector<unsigned long long > & q_digits)
{
	int N_size = p_digits.size() + q_digits.size();
	//expected_buffer_size=N_size;
	vector<unsigned long long> N(N_size, 0);
	int N_index = 0;
	unsigned long long int remainder = 0;
	for (int i = 0;i<p_digits.size();i++)
	{
		for (int j = 0;j<q_digits.size();j++)
		{
			//cout<<endl<<p_digits[p_digits.size()-i-1]<<endl<<q_digits[q_digits.size()-j-1];
			unsigned long long int  result = p_digits[p_digits.size() - i - 1] * q_digits[q_digits.size() - j - 1];
			if (result>999999)
			{
				N[N_size - 1 - (N_index + j)] += result % 1000000000 + remainder;
				//cout <<endl<<"N["<<N_size-1-(N_index+j)<<"] ="<< N[N_size-1-(N_index+j)];
				remainder = result / 1000000000;

				if (N[N_size - 1 - (N_index + j)]>999999999)
				{
					remainder += N[N_size - 1 - (N_index + j)] / 1000000000;
					N[N_size - 1 - (N_index + j)] = N[N_size - 1 - (N_index + j)] % 1000000000;

				}
				//cout <<endl<<"N["<<N_size-1-(N_index+j)<<"] ="<< N[N_size-1-(N_index+j)];

			}
			else
			{
				N[N_size - 1 - (N_index + j)] += result + remainder;
				remainder = 0;
				if (N[N_size - 1 - (N_index + j)]>999999999)
				{
					N[N_size - 1 - (N_index + j)] = N[N_size - 1 - (N_index + j)] % 1000000000;
					remainder++;
				}
			}

		}
		N[N_size - 1 - (i + q_digits.size())] += remainder;
		//cout<<endl<<"N["<<N_size-1-(i+q_digits.size())<<"] ="<<N[N_size-1-(i+q_digits.size())];
		remainder = 0;
		N_index++;
	}
	while (N.size() > 1)
	{
		if (N[0] == 0)
		{
			N.erase(N.begin());
		}
		else
			break;
	}
	return N;
}
void print(vector<unsigned long long> & N)
{
	bool number_start = false;
	for (int i = 0;i<N.size();i++)
	{
		if (N[i] == 0 && number_start == false)
		{
			continue;
		}
		else if (N[i] != 0 && number_start == false)
		{
			number_start = true;
			cout << N[i];
			continue;
		}
		number_start = true;
		if (i != 0 && N[i] <= 99999999)
			cout << "0";
		if (i != 0 && N[i] <= 9999999)
			cout << "0";
		if (i != 0 && N[i] <= 999999)
			cout << "0";
		if (i != 0 && N[i] <= 99999)
			cout << "0";
		if (i != 0 && N[i] <= 9999)
			cout << "0";
		if (i != 0 && N[i] <= 999)
			cout << "0";
		if (i != 0 && N[i] <= 99)
			cout << "0";
		if (i != 0 && N[i] <= 9)
			cout << "0";
		cout << N[i];
	}
	cout<<endl;
}

vector<unsigned long long> subtract(vector<unsigned long long > p_digits, vector <unsigned long long> & q_digits)
{
	vector<unsigned long long > result(p_digits.size(), 0);
	for (int i = 0; i < p_digits.size(); i++)
	{
		if (i<q_digits.size())
		{
			//cout<<endl<<"p_digits["<<p_buffer_size-1-i<<"]= "<<p_digits[p_buffer_size-1-i];
			//cout<<endl<<"q_digits["<<q_buffer_size-1-i<<"]= "<<q_digits[q_buffer_size-1-i];
			if (p_digits[p_digits.size() - 1 - i]<q_digits[q_digits.size() - 1 - i])
			{
				p_digits[p_digits.size() - 1 - i] += 1000000000;
				p_digits[p_digits.size() - 1 - i - 1]--;
			}
			result[p_digits.size() - 1 - i] = p_digits[p_digits.size() - 1 - i] - q_digits[q_digits.size() - 1 - i];
		}
		else
			result[p_digits.size() - 1 - i] = p_digits[p_digits.size() - 1 - i];
		//cout<<endl<<"result["<<p_buffer_size-1-i<<"]= "<<result[p_buffer_size-1-i];
	}
	//int i=0;
	while (result.size() > 1)
	{
		if (result[0] == 0)
		{
			result.erase(result.begin());
		}
		else
			break;
	}

	return result;
}
vector<unsigned long long> digits(string p, int buffer_size)
{
	vector<string> p_buffer;
	for (int i = 0;i<buffer_size;i++)
	{

		if (i == buffer_size - 1)
			p_buffer.push_back(p.substr(0, p.size() - i * 9));
		else
			p_buffer.push_back(p.substr(p.size() - (i + 1) * 9, 9));
	}
	reverse(p_buffer.begin(), p_buffer.end());
	unsigned long long * p_digit = new unsigned long long[0];
	char * ptr;
	vector <unsigned long long> p_digits(buffer_size);
	for (int i = 0;i<buffer_size;i++)
	{
		p_digit[0] = strtoul(p_buffer[i].c_str(), &ptr, 10);
		p_digits[i] = p_digit[0];
	}

	return p_digits;

}
bool is_bigger_than(const vector<unsigned long long > & temp_p_digits,const vector<unsigned long long > & q_digits, int temp_p_buffer_size, int q_buffer_size)
{
	if (temp_p_buffer_size<q_buffer_size)
		return false;
	else if (temp_p_buffer_size>q_buffer_size)
		return true;
	for (int i = 0;i<temp_p_buffer_size;i++)
	{
		if (temp_p_digits[i]>q_digits[i])
			return true;
		else if (temp_p_digits[i]<q_digits[i])
			return false;
	}
	return true;
}
bool is_bigger_than(const vector<unsigned long long> & p_digits, const vector<unsigned long long> & q_digits)
{
	if (p_digits.size()<q_digits.size())
		return false;
	else if (p_digits.size()>q_digits.size())
		return true;
	for (int i = 0;i<p_digits.size();i++)
	{
		if (p_digits[i]>q_digits[i])
			return true;
		else if (p_digits[i]<q_digits[i])
			return false;
	}
	return true;
}
vector<unsigned long long> add(vector<unsigned long long> & p_digits, vector<unsigned long long> & q_digits)
{
	vector<unsigned long long> * first;
	vector<unsigned long long> * second;
	int first_buffer_size, second_buffer_size;
	int expected_add_size;
	if (p_digits.size() >= q_digits.size())
	{
		expected_add_size = p_digits.size() + 1;
		first = &p_digits;
		second = &q_digits;
		first_buffer_size = p_digits.size();
		second_buffer_size = q_digits.size();

	}
	else
	{
		expected_add_size = q_digits.size() + 1;
		first = &q_digits;
		second = &p_digits;
		first_buffer_size = q_digits.size();
		second_buffer_size = p_digits.size();
	}
	vector<unsigned long long> add_result = vector<unsigned long long>(expected_add_size, 0);
	for (int i = 0;i<first_buffer_size;i++)
	{

		if (i<second_buffer_size)
			add_result[expected_add_size - 1 - i] += (*first)[first_buffer_size - 1 - i] + (*second)[second_buffer_size - 1 - i];
		else
			add_result[expected_add_size - 1 - i] += (*first)[first_buffer_size - 1 - i];
		if (add_result[expected_add_size - 1 - i]>999999999)
		{
			add_result[expected_add_size - 2 - i] += add_result[expected_add_size - 1 - i] / 1000000000;
			add_result[expected_add_size - 1 - i] = add_result[expected_add_size - 1 - i] % 1000000000;
		}
	}
	int i = 0;
	while (add_result[0] == 0)
	{
		add_result.erase(add_result.begin());

	}
	//add_result.erase(add_result.begin(), add_result.begin()+i);
	return add_result;
}
vector<unsigned long long>double_division(vector <unsigned long long > p_digits, const vector< unsigned long long > & q_digits, vector<unsigned long long > & divison_result)
{
	//cout<<endl<<"from double divison";
	//cout<<endl<<"q_digits=";
	//print(q_digits);
	if (!divison_result.empty())
		divison_result.clear();
	divison_result.push_back(0);
	vector<vector <unsigned long long>> factors;//factorslist for itterting over the map in a sorted pattern 
	vector<vector<unsigned long long>>results;
	vector<unsigned long long >factor(1, 1);
	map<vector<unsigned long long >, vector< unsigned long long >>mapping;// factor and mapping results 
	mapping[factor] = q_digits;
	factors.push_back(factor);

	int temp;
	vector<unsigned long long > result = q_digits;
	results.push_back(result);

	while (is_bigger_than(p_digits, q_digits))
	{
		if (is_bigger_than(p_digits, q_digits, q_digits.size(), q_digits.size()))
			temp = q_digits.size();
		else
			temp = q_digits.size() + 1;
		while (is_bigger_than(p_digits, result, temp, result.size()))
		{
			//construct the map	
			vector<unsigned long long> t(1, 2);
			factor = Multiply(factor, t);
			result = add(result, result);
			factors.push_back(factor);
			//cout<<endl<<"map_mul= ";
			//print(result);
			mapping[factor] = result;
			results.push_back(result);
		}
		int i;
		for (i = 0;i<factors.size();i++)//get the element to be subtracted 
		{
			if (!is_bigger_than(p_digits, results[i], temp, results[i].size()))
			{
				i--;
				break;
			}
		}
		if (i == -1)
			break;
		vector<unsigned long long>division_factor;
		for (int j = 0;j<factors[i].size();j++)
		{
			division_factor.push_back(factors[i][j]);
		}
		vector<unsigned long long> sub_array(p_digits.size(), 0);
		if (results[i].size()<p_digits.size())
		{
			int padding = p_digits.size() - results[i].size();
			for (int j = 0;j<padding;j++)
				division_factor.push_back(0);
			for (int j = 0;j<results[i].size();j++)
				sub_array[j] = results[i][j];
		}

		else
			sub_array = mapping[factors[i]];
		//cout<<endl<<"division_factor =";
		//print(division_factor);
		//cout<<endl<<"p_digits_before sub = ";
		//print(p_digits);
		//int ai_7aga;
		if (!is_bigger_than(p_digits, sub_array))
		{
			sub_array.pop_back();
			division_factor.pop_back();
		}
		p_digits = subtract(p_digits, sub_array);
		//cout<<endl<<"sub_aray            = ";
		//print(sub_array); 

		while (p_digits[0] == 0 && p_digits.size() > 1)//eleminate left zeros when they appear
		{
			p_digits.erase(p_digits.begin());
		}

		//cout<<endl<<"p_digits           = ";
		//print(p_digits);
		divison_result = add(division_factor, divison_result);
		//cout<<endl<<"divison_result      = ";
		//print(divison_result);

	}
	//	cout<<endl<<"p_digits            = ";
	//print(p_digits);
	return p_digits;

}
vector<unsigned long long> divison(vector<unsigned long long> p_digits,  vector<unsigned long long > & q_digits, vector<unsigned long long>&division_result)
{

	unsigned long long temp;
	do
	{

		// int sub_index=0;
		while (p_digits[0] == 0)
		{
			p_digits.erase(p_digits.begin());
			if (p_digits.size() == 0)
				break;
		}
		if (p_digits.size() == 0)
			break;
		if (p_digits.size()<q_digits.size() || (p_digits.size() == q_digits.size() && p_digits[0]<q_digits[0]))
			break;
		if (p_digits[0]<q_digits[0])
		{
			temp = p_digits[0] * 1000000000 + p_digits[1];
			//division_result.push_back(0);
			// sub_index=1;
		}
		else
			temp = p_digits[0];
		unsigned long long int factor = (temp / q_digits[0]);
		if (factor == 0)
			break;
		vector<unsigned long long> temporary(1, factor);
		vector<unsigned long long> mul = Multiply(temporary, q_digits);
		vector<unsigned long long> sub_array(p_digits.size(), 0);
		if (mul.size()<p_digits.size())
		{
			int padding = p_digits.size() - mul.size();
			for (int i = 0;i<mul.size();i++)
				sub_array[i] = mul[i];
			for (int i = 0;i<padding;i++)
				sub_array[i + mul.size()] = 0;
		}
		else
			sub_array = mul;
		while (!is_bigger_than(p_digits, sub_array))
		{
			///double division			
			cout << endl << "from division" << endl << "p_digits = ";
			print(p_digits);
			return double_division(p_digits, q_digits, division_result);

			// return result of double_division;
		}
		division_result.push_back(factor);
		if (factor == 0)
			break;

		p_digits = subtract(p_digits, sub_array);
		//cout<<endl<<"temp= "<<temp<<endl<<"q_digits[0] ="<<q_digits[0];
		// cout<<endl<<"factor= "<<factor;
		// cout<<endl<<"sub_array =";
		// print(sub_array);
		// cout<< endl<<"temp_p_buffer_size= "<<temp_p_buffer_size;
		// cout<<endl<<"temp_p_digits";
		// print(p_digits);
		// cout<<endl;

	} while (1);
	return p_digits;
}
vector<unsigned long long>exponentiation(unsigned long long constant, vector<unsigned long long> power, vector<unsigned  long long>remainderOf)
{
	vector<unsigned long long>temp(1, 1);
	vector<vector<unsigned long long>> results;
	vector<vector<unsigned long long>> powers;
	vector<unsigned long long> temp_power(1, 1);
	powers.push_back(temp);
	temp[0] = 2;
	vector<unsigned long long>result(1, constant);
	results.push_back(result);
	while (is_bigger_than(power, temp_power))
	{

		temp_power = Multiply(temp_power, temp);
		powers.push_back(temp_power);
		result = Multiply(result, result);
		if (is_bigger_than(result, remainderOf))
		{
			vector<unsigned long long > notnecessary;
			result = double_division(result, remainderOf, notnecessary);
		}
		results.push_back(result);

	}
	//vector<unsigned long long>ai_7aga(1,0);
	vector<unsigned long long>output(1, 1);
	int i = powers.size() - 1;
	while (power.size() != 0)
	{
		/*if(power.size()==1)
		{
		cout<<endl;
		}*/
		if (is_bigger_than(power, powers[i]))
		{
			output = Multiply(output, results[i]);
			if (is_bigger_than(output, remainderOf))
			{
				vector<unsigned long long> unnecessary;
				output = double_division(output, remainderOf, unnecessary);
			}
			power = subtract(power, powers[i]);
			while (power[0] == 0)
			{
				power.erase(power.begin());
				if (power.size() == 0)
					break;
			}
			//cout << endl << "power =";
			//print(power);
		}
		i--;
	}

	return output;
}
vector<unsigned long long>exponentiation(vector<unsigned long long >constant, vector<unsigned long long> power, vector<unsigned  long long>remainderOf)
{
	vector<unsigned long long>temp(1, 1);
	vector<vector<unsigned long long>> results;
	vector<vector<unsigned long long>> powers;
	vector<unsigned long long> temp_power(1, 1);
	powers.push_back(temp);
	temp[0] = 2;
	vector<unsigned long long>result = constant;
	results.push_back(result);
	while (is_bigger_than(power, temp_power))
	{

		temp_power = Multiply(temp_power, temp);
		powers.push_back(temp_power);
		result = Multiply(result, result);
		if (is_bigger_than(result, remainderOf))
		{
			vector<unsigned long long > notnecessary;
			result = double_division(result, remainderOf, notnecessary);
		}
		results.push_back(result);

	}
	//vector<unsigned long long>ai_7aga(1,0);
	vector<unsigned long long>output(1, 1);
	int i = powers.size() - 1;
	while (power.size() != 0)
	{
		/*if(i==-1)
		{
		cout<<endl;
		}*/
		if (is_bigger_than(power, powers[i]))
		{
			output = Multiply(output, results[i]);
			if (is_bigger_than(output, remainderOf))
			{
				vector<unsigned long long> unnecessary;
				output = double_division(output, remainderOf, unnecessary);
			}
			power = subtract(power, powers[i]);

			while (power[0] == 0)
			{
				power.erase(power.begin());
				if (power.size() == 0)
					break;
			}
			//cout << endl << "power =";
			//print(power);
		}
		i--;
	}
	return output;
}
bool isPrime(vector<unsigned long long> digits)
{
	if(digits.size()==1&&digits[0]==2)
		return true;
	

	vector<unsigned long long>temp(1, 1);
	vector<unsigned long long>digits_1 = subtract(digits, temp);
	temp[0] = 2;
	vector<unsigned long long>q;
	double_division(digits_1, temp, q);
	int k = 1;
	while (q[q.size() - 1] % 2 == 0)
	{
		double_division(q, temp, q);
		k++;
	}
	//cout << endl << "q =";
	//print(q);
	for (int i = 0;i<5;i++)
	{
		bool achieved = false;
		unsigned long long a;
		if (digits.size()>1)
			a = rand() % 1000000000;
		else 
			a=rand() % digits[0] ;
		if(a==0)
			a=1;
		vector<unsigned long long>output = exponentiation(a, q, digits);
		if (output.size() == 1 && output[0] == 1)
		{
			achieved = true;
			continue;
		}
		vector<unsigned long long>dummy = subtract(digits, output);
		while (dummy[0] == 0)
			dummy.erase(dummy.begin());
		if (dummy.size() == 1 && dummy[0] == 1)
		{
			achieved = true;
			continue;
		}
		if (k>1)
		{
			for (int j = 1;j<k;j++)
			{
				//q = Multiply(q, temp);
				output = Multiply(output, output);
				vector<unsigned long long>unnecessary;
				output = double_division(output, digits, unnecessary);
				dummy = subtract(digits, output);
				while (dummy[0] == 0)
					dummy.erase(dummy.begin());
				if (dummy.size() == 1 && dummy[0] == 1)
				{
					achieved = true;
					break;
				}

			}
		}
		if (!achieved)
			return false;
	}
	return true;

}

vector<unsigned long long> extended_ecluid(vector<unsigned long long > & p_digits, vector<unsigned long long > & phi)
{
	vector<unsigned long long > A1(1, 1);
	vector<unsigned long long > A2(1, 0);
	vector<unsigned long long > A3 = phi;
	vector<unsigned long long > B1(1, 0);
	vector<unsigned long long > B2(1, 1);
	vector<unsigned long long > B3 = p_digits;
	bool sign = false;
	while (true)
	{
		vector<unsigned long long > Q;
		double_division(A3, B3, Q);
		vector<unsigned long long> T1 = Multiply(Q, B1);
		T1 = add(A1, T1);
		vector<unsigned long long> T2 = Multiply(Q, B2);
		T2 = add(A2, T2);
		vector<unsigned long long> T3 = Multiply(Q, B3);
		T3 = subtract(A3, T3);
		A1 = B1;
		A2 = B2;
		A3 = B3;
		B1 = T1;
		B2 = T2;
		B3 = T3;
		sign = !sign;
		if (B3.size() == 1 && (B3[0] == 1 || B3[0] == 0))
			break;
	}
	if (sign == true)
		B2 = subtract(phi, B2);
	else
		B1 = subtract(phi, B1);

	return B2;
}

int main()
{
	//clock_t start=clock();
	string p, q, e;
	//p="12369571528747655798110188786567180759626910465726920556567298659370399748072366507234899432827475865189642714067836207300153035059472237275816384410077871";
	//q="2065420353441994803054315079370635087865508423962173447811880044936318158815802774220405304957787464676771309034463560633713497474362222775683960029689473";
	vector<unsigned long long > p_digits, q_digits, e_digits, N, phi, d, c, m;
	while (1)
	{
		string temp = "";
		getline(cin, temp);
		if (temp.substr(0, 2) == "P=" || temp.substr(0, 2) == "p=")
		{
			p = temp.erase(0, 2);
			int p_buffer_size = ceil((float)p.size() / 9.0);
			p_digits = digits(p, p_buffer_size);
		}
		else if (temp.substr(0, 2) == "Q=" || temp.substr(0, 2) == "q=")
		{
			q = temp.erase(0, 2);
			int q_buffer_size = ceil((float)q.size() / 9.0);
			q_digits = digits(q, q_buffer_size);
		}
		else if (temp.substr(0, 2) == "E=" || temp.substr(0, 2) == "e=")
		{
			e = temp.erase(0, 2);
			int e_buffer_size = ceil((float)e.size() / 9.0);
			e_digits = digits(e, e_buffer_size);
		}
		else if (temp == "IsPPrime")
		{
			if (isPrime(p_digits))
				cout << "Yes" << endl;
			else
				cout << "No" << endl;
		}
		else if (temp == "IsQPrime")
		{
			if (isPrime(q_digits))
				cout << "Yes" << endl;
			else
				cout << "No" << endl;
		}
		else if (temp == "PrintN")
		{
			N = Multiply(p_digits, q_digits);
			print(N);
		}
		else if (temp == "PrintPhi")
		{
			vector<unsigned long long> one(1, 1);
			vector<unsigned long long> p1 = subtract(p_digits, one);
			vector<unsigned long long> q1 = subtract(q_digits, one);
			phi = Multiply(p1, q1);
			print(phi);
		}
		else if (temp == "Quit")
		{
			//cout<<(clock() - start ) / (double) CLOCKS_PER_SEC;
			return 0;
		}
		else if (temp == "PrintD")
		{
			if(phi.size()==0)
			{
				vector<unsigned long long> one(1, 1);
				vector<unsigned long long> p1 = subtract(p_digits, one);
				vector<unsigned long long> q1 = subtract(q_digits, one);
				phi = Multiply(p1, q1);
			}

			d = extended_ecluid(e_digits, phi);
			print(d);
		}
		if (temp.substr(0, 15) == "EncryptPublic=<"&&temp[temp.size() - 1] == '>')
		{
			string message = temp.erase(0, 15);
			message = message.erase(message.size() - 1, 1);
			int message_buffer_size = ceil((float)message.size() / 9.0);
			vector<unsigned long long >message_digits = digits(message, message_buffer_size);
			if(N.size()==0)
			{
				N = Multiply(p_digits, q_digits);
			}
			c = exponentiation(message_digits, e_digits, N);
			print(c);
		}
		if (temp.substr(0, 16) == "EncryptPrivate=<"&&temp[temp.size() - 1] == '>')
		{
			string message = temp.erase(0, 16);
			message = message.erase(message.size() - 1, 1);
			int message_buffer_size = ceil((float)message.size() / 9.0);
			vector<unsigned long long >message_digits = digits(message, message_buffer_size);
			if(d.size()==0)
			{
				if(phi.size()==0)
				{
					vector<unsigned long long> one(1, 1);
					vector<unsigned long long> p1 = subtract(p_digits, one);
					vector<unsigned long long> q1 = subtract(q_digits, one);
					phi = Multiply(p1, q1);
				}

				d = extended_ecluid(e_digits, phi);	
			}
			m = exponentiation(message_digits, d, N);
			print(m);
		}
	}
	//string p = "12369571528747655798110188786567180759626910465726920556567298659370399748072366507234899432827475865189642714067836207300153035059472237275816384410077871";
	//string q = "2065420353441994803054315079370635087865508423962173447811880044936318158815802774220405304957787464676771309034463560633713497474362222775683960029689473";
	//string e = "65537";
	int p_buffer_size = ceil((float)p.size() / 9.0);
	int q_buffer_size = ceil((float)q.size() / 9.0);
	int e_buffer_size = ceil((float)e.size() / 9.0);
	//vector<unsigned long long> p_digits = digits(p, p_buffer_size);
	//vector<unsigned long long> q_digits = digits(q, q_buffer_size);
	//vector<unsigned long long> e_digits = digits(e, e_buffer_size);
	//vector<unsigned long long> N = Multiply(p_digits, q_digits);
	vector<unsigned long long> one(1, 1);
	vector<unsigned long long> p1 = subtract(p_digits, one);
	vector<unsigned long long> q1 = subtract(q_digits, one);
	//vector<unsigned long long> phi = Multiply(p1, q1);
	//print(N);
	///////////////////////////////////////subtract////////////////////////////////////
	//subtract p-q
	// unsigned long long *result =subtract(p_digits,q_digits,p_buffer_size,q_buffer_size);
	// print(p_buffer_size,result);
	//////////////////////////////////////division(remainder)//////////////////////////////////////
	//divide p%q  ///lazem p tkon akbar mn q 
	/*	 int temp_p_buffer_size=p_buffer_size;//must put it in another temp for the call by refrence to work
	vector<unsigned long long>division_result;
	// unsigned long long * reslut1=divison(p_digits,q_digits,temp_p_buffer_size,q_buffer_size,division_result);
	//print(temp_p_buffer_size,reslut1) ;
	int expected_add_size=p_buffer_size+1;
	unsigned long long *result2= add (p_digits,q_digits,p_buffer_size,q_buffer_size,expected_add_size);
	cout<<endl<<"addition=";
	print (expected_add_size,result2);*/
	//////////////////////////////////////miller rabin test/////////////////////////////////////////////////
	//ispPrime 
	if (isPrime(p_digits))
		cout << endl << "achieved";
	if (isPrime(q_digits))
		cout << "q_achieved";

	//	vector<unsigned long long> d = extended_ecluid(e_digits, phi);
	print(d);
	/*
	vector<unsigned long long > temp(1, 1);
	temp[0] = 1;
	int p_1_buffer_size = p_buffer_size;
	vector<unsigned long long> p_1 = subtract(p_digits, temp);
	temp[0] = 2;
	vector<unsigned long long >qq;
	p_1 = divison(p_1, temp, qq);
	cout << endl << "remainer= ";
	print(p_1);
	cout << endl << "divison = ";
	print(qq);
	//	print(p_1);
	int k = 1;
	while (qq[qq.size() - 1] % 2 == 0)
	{
	int size = qq.size();
	divison(qq, temp, qq);
	k++;
	}
	cout << endl << "q =";
	print(qq);
	//std::map <string, char> grade_list;
	map<vector< unsigned long long >, vector< unsigned long long > >mapping;
	int a = rand() % 1000000000;
	vector<unsigned long long> output = exponentiation(a, qq, p_digits);
	cout << "power_output= ";
	print(output);
	vector<unsigned long long > a_power;
	//fill(a_power,a_power+p_buffer_size,0);
	vector<unsigned long long > result;
	a_power.push_back(1);
	result.push_back(a);
	//cout<<endl<<*a_power<<"   "<<*result;
	//vector <int> power_sizes,result_sizes;//for printing
	//power_sizes.push_back(1);
	//result_sizes.push_back(1);
	mapping[a_power] = result;
	int m = 0;
	while (is_bigger_than(qq, a_power))
	{
	//int expected_a_power_size=power_sizes[m]+1;
	a_power = Multiply(a_power, temp);
	//int expeted_result_size=2*result_sizes[m];
	result = Multiply(result, result);
	if (is_bigger_than(result, p_digits))
	{
	vector<unsigned long long > notnecessary;
	result = double_division(result, p_digits, notnecessary);
	cout << endl << "division_result =";
	print(notnecessary);
	}
	mapping[a_power] = result;
	//power_sizes.push_back(expected_a_power_size);
	//result_sizes.push_back(expeted_result_size);
	cout << endl << "mapping[";
	print(a_power);
	cout << "] = ";
	print(result);
	m++;
	}
	int j = 0;
	/*for (map< unsigned long long * ,unsigned long long * >::iterator i= mapping.begin();i !=mapping.end();i++)
	{
	cout <<endl<<"mapping[";
	print(power_sizes[j],i->first);
	cout<<"] = ";
	print(result_sizes[j],i->second);
	j++;
	}*/
	//////////////////////////////////////print/////////////////////////////////////////
	// print (p_buffer_size+q_buffer_size,N);
	// cout<<endl;
	// print(p_buffer_size,result);*/

	return 0;
}