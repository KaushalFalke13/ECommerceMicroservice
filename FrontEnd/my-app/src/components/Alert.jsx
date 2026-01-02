import { CheckCircle, AlertCircle } from "lucide-react";

const Alert = ({ type, message }) => {
  if (!message) return null;
    return(
          <div className={`mb-4 p-4 rounded-lg flex items-center gap-3 ${
            type === 'success' 
              ? 'bg-green-50 text-green-800 border border-green-200' 
              : 'bg-red-50 text-red-800 border border-red-200'
          }`}>
            {type === 'success' ? (
              <CheckCircle className="h-5 w-5" />
            ) : (
              <AlertCircle className="h-5 w-5" />
            )}
            <p className="font-medium">{message}</p>
          </div>
        );
};



export default Alert;
